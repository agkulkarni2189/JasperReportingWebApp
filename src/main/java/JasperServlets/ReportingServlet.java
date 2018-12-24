package JasperServlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DynamicJasperLibrary.ReportingUtility;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.domain.DynamicReport;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.type.PdfPrintScalingEnum;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;
import ar.com.fdvs.dj.core.layout.*;
import UtilityLibrary.DBConnectionUtility;

/**
 * Servlet implementation class ReportingServlet
 */
public class ReportingServlet extends HttpServlet {
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private DBConnectionUtility DBUtility = null;

	/**
	 * @throws Exception
	 * @see HttpServlet#HttpServlet()
	 */
	public ReportingServlet() throws Exception {
		super();
		// TODO Auto-generated constructor stub
		try {
			DBUtility = new DBConnectionUtility("com.microsoft.sqlserver.jdbc.SQLServerDriver",
					"jdbc:sqlserver://192.168.1.88:1433;databaseName=haziradb", "sa", "sipl@123");
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HashMap<String, String> Location_Master = new HashMap<String, String>();
		HashMap<String, String> Lane_Type_Master = new HashMap<String, String>();
		HashMap<String, String> Lane_Master = new HashMap<String, String>();
		String LocationID = null;
		String LaneTypeID = null;
		String LaneID = null;

		ServletContext context = this.getServletConfig().getServletContext();
		response.setContentType("text/html");
		StringWriter sw = new StringWriter();

		try {
			ResultSet rs = DBUtility.getRecordsFromDB("SELECT location_id, location_name from location_master");

			if (request.getParameterMap().containsKey("Location"))
				LocationID = request.getParameter("Location");

			if (request.getParameterMap().containsKey("Lane_Type"))
				LaneTypeID = request.getParameter("Lane_Type");

			if (request.getParameterMap().containsKey("Lane"))
				LaneID = request.getParameter("Lane");

			while (rs.next()) {
				Location_Master.put(rs.getString("location_id"), rs.getString("location_name"));
			}

			request.setAttribute("Location_Master", Location_Master);
			rs = DBUtility.getRecordsFromDB("SELECT lane_type_id, lane_type_name from lane_type_master");

			while (rs.next()) {
				Lane_Type_Master.put(rs.getString("lane_type_id"), rs.getString("lane_type_name"));
			}

			request.setAttribute("Lane_Type_Master", Lane_Type_Master);
			rs = DBUtility.getRecordsFromDB("SELECT lane_id, lane_name from lane_master");

			while (rs.next()) {
				Lane_Master.put(rs.getString("lane_id"), rs.getString("lane_name"));
			}

			request.setAttribute("Lane_Master", Lane_Master);
			request.setAttribute("ReportTitle", "Transactions Report");

			PrintWriter JasperReportWriter = new PrintWriter(new String());
			JasperPrint jp = getFilteredReportPrint(LocationID, LaneTypeID, LaneID);

			request.getSession().setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jp);
			HtmlExporter exporter = new HtmlExporter();
			request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jp);
			exporter.setExporterInput(new SimpleExporterInput(jp));

			SimpleHtmlExporterOutput output = new SimpleHtmlExporterOutput(sw);
			output.setImageHandler(new WebHtmlResourceHandler("servlets/image?image={0}"));
			exporter.setExporterOutput(output);

			exporter.exportReport();
			request.setAttribute("MainReport", sw.toString());

			if (request.getParameterMap().containsKey("ExportOption")) {
				int ExportOption = Integer.parseInt((String) request.getParameter("ExportOption"));
				ByteArrayOutputStream ReportStream = new ByteArrayOutputStream();

				if (ExportOption > 0) {
					if (ExportOption == 1) {
						ReportStream = ExportReport(ExportOption, jp);
						response.setContentType("application/pdf");
						response.setHeader("Content-Length", String.valueOf(ReportStream.size()));
						response.setHeader("Content-Disposition", "attachment; filename=TransactionReport.pdf");
					}

					if (ExportOption == 2) {
						ReportStream = ExportReport(ExportOption, jp);
						response.setContentType("application/xlsx");
						response.setHeader("Content-Length", String.valueOf(ReportStream.size()));
						response.setHeader("Content-Disposition", "attachment; filename=TransactionReport.xlsx");
					}

					if (ExportOption == 3) {
						JasperPrintManager.printReport(jp, true);
					}

					if (ReportStream.size() > 0) {
						ServletOutputStream responseOutputStream = response.getOutputStream();
						responseOutputStream.write(ReportStream.toByteArray());
						responseOutputStream.close();
					}
				}
			}

		} catch (JRException jre) {
			// writer.println(jre.getMessage());
			request.setAttribute("Exception", jre.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace(writer);
			request.setAttribute("Exception", e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace(writer);
			request.setAttribute("Exception", e.getMessage());
		}

		request.getRequestDispatcher("/WEB-INF/TransactionsReport.jsp").forward(request, response);
	}

	// @Override
	// protected void doPost(HttpServletRequest request, HttpServletResponse
	// response)
	// throws ServletException, IOException {
	// try {
	//
	// String Location_ID= null;
	// String Lane_Type_ID = null;
	// String Lane_ID = null;
	//
	// if(request.getParameter("Location") != null &&
	// request.getParameter("Location") != "")
	// Location_ID = request.getParameter("Location");
	//
	// if(request.getParameter("Lane_Type") != null &&
	// request.getParameter("Lane_Type") != "")
	// Lane_Type_ID = request.getParameter("Lane_Type");
	//
	// if(request.getParameter("Lane") != null && request.getParameter("Lane") !=
	// "")
	// Lane_ID = request.getParameter("Lane");
	//
	// PrintWriter JasperReportWriter = new PrintWriter(new String());
	// JasperPrint jp = getFilteredReportPrint(Location_ID, Lane_Type_ID, Lane_ID);
	//
	// if(request.getParameter("ExportOption") != null)
	// {
	// int ExportOption = Integer.parseInt((String)
	// request.getParameter("ExportOption"));
	// ByteArrayOutputStream ReportStream = new ByteArrayOutputStream();
	//
	// if (ExportOption > 0)
	// {
	// if(ExportOption == 1)
	// {
	// ReportStream = ExportReport(ExportOption, jp);
	// response.setContentType("application/pdf");
	// response.setHeader("Content-Length", String.valueOf(ReportStream.size()));
	// response.setHeader("Content-Disposition", "attachment;
	// filename=TransactionReport.pdf");
	// request.setAttribute("TransactionReportByteArray", ReportStream);
	// }
	//
	// if(ExportOption == 2)
	// {
	// ReportStream = ExportReport(ExportOption, jp);
	// response.setContentType("application/xlsx");
	// response.setHeader("Content-Length", String.valueOf(ReportStream.size()));
	// response.setHeader("Content-Disposition", "attachment;
	// filename=TransactionReport.xlsx");
	// request.setAttribute("TransactionReportByteArray", ReportStream);
	// }
	//
	// if (ExportOption == 3) {
	// JasperPrintManager.printReport(jp, true);
	// }
	//
	// if(ReportStream.size() > 0)
	// {
	// ServletOutputStream responseOutputStream = response.getOutputStream();
	// responseOutputStream.write(ReportStream.toByteArray());
	// responseOutputStream.close();
	// }
	// }
	// }
	//
	// request.getSession().setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
	// jp);
	// HtmlExporter exporter = new HtmlExporter();
	// request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
	// jp);
	// exporter.setExporterInput(new SimpleExporterInput(jp));
	//
	// SimpleHtmlExporterOutput output = new
	// SimpleHtmlExporterOutput(JasperReportWriter);
	// output.setImageHandler(new
	// WebHtmlResourceHandler("servlets/image?image={0}"));
	// exporter.setExporterOutput(output);
	//
	// exporter.exportReport();
	// request.setAttribute("MainReport", JasperReportWriter.toString());
	// } catch (JRException jre) {
	// request.setAttribute("Exception", jre.getMessage());
	// // writer.println(jre.getMessage());
	// } catch (ClassNotFoundException e) {
	// // TODO Auto-generated catch block
	// request.setAttribute("Exception", e.getMessage());
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// // e.printStackTrace(writer);
	// request.setAttribute("Exception", e.getMessage());
	// }
	//
	// request.getRequestDispatcher("/WEB-INF/TransactionReport.jsp").forward(request,
	// response);
	// }

	private ByteArrayOutputStream ExportReport(int option, JasperPrint jp) throws Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyyhh:mm:ss");
		ByteArrayOutputStream ReportStream = new ByteArrayOutputStream();

		if (option == 1) {
			JRPdfExporter PdfExporter = new JRPdfExporter();
			PdfExporter.setExporterInput(new SimpleExporterInput(jp));
			PdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(ReportStream));
			SimplePdfExporterConfiguration config = new SimplePdfExporterConfiguration();
			config.setPrintScaling(PdfPrintScalingEnum.DEFAULT);
			PdfExporter.setConfiguration(config);
			PdfExporter.exportReport();
		}

		if (option == 2) {
			JRXlsxExporter ExcelExporter = new JRXlsxExporter();
			ExcelExporter.setExporterInput(new SimpleExporterInput(jp));
			ExcelExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(ReportStream));
			ExcelExporter.exportReport();
		}

		return ReportStream;
	}

	private JasperPrint getFilteredReportPrint(String LocationID, String LaneTypeID, String LaneID) throws Exception {
		StringBuilder sb = new StringBuilder();
		ReportingUtility ru = new ReportingUtility();
		DynamicReport dr = ru.buildReport(LocationID, LaneTypeID, LaneID);
		ResultSet rset = DBUtility.getRecordsFromDB(dr.getQuery().getText());
		JRResultSetDataSource jrd = new JRResultSetDataSource(rset);
		JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), jrd);
		return jp;
	}

}
