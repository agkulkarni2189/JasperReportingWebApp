package DynamicJasperLibrary;

import java.awt.Color;
import java.sql.Date;
import java.sql.Timestamp;

import org.exolab.castor.types.DateTime;

import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Stretching;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.builders.*;
import ar.com.fdvs.dj.core.layout.*;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
;
public class ReportingUtility {

	public ReportingUtility() {
		super();
	}
		// TODO Auto-generated constructor stub
		
	public DynamicReport buildReport(String QueryConditions) throws Exception
	{	
		String DBQuery = "WITH Lane_Trans_CTE\r\n" + 
				"AS\r\n" + 
				"(\r\n" + 
				"	SELECT \r\n" + 
				"		tt.rfid_transaction_id,\r\n" + 
				"		tt.tag_id,\r\n" + 
				"		tt.boom_up_request_source,\r\n" + 
				"		tt.ack_sent_time,\r\n" + 
				"		tt.hht_boomup_request_recd_time,\r\n" + 
				"		tt.rfid_boomup_request_recd_time,\r\n" + 
				"		lm.lane_name,\r\n" + 
				"		lm.lane_id,\r\n" + 
				"		lm.lane_type_id \r\n" + 
				"	FROM\r\n" + 
				"		rfid_transaction_table tt\r\n" + 
				"	LEFT OUTER JOIN\r\n" + 
				"		lane_master lm\r\n" + 
				"	ON\r\n" + 
				"		tt.lane_id=lm.lane_id\r\n" + 
				"),\r\n" + 
				"Lane_Type_CTE AS\r\n" + 
				"(\r\n" + 
				"	SELECT \r\n" + 
				"		ltc.*, \r\n" + 
				"		ltm.lane_type_name \r\n" + 
				"	FROM \r\n" + 
				"		Lane_Trans_CTE ltc \r\n" + 
				"	LEFT OUTER JOIN \r\n" + 
				"		lane_type_master ltm \r\n" + 
				"	ON \r\n" + 
				"		ltc.lane_type_id=ltm.lane_type_id\r\n" + 
				"),\r\n" + 
				"Location_Type_CTE AS\r\n" + 
				"(\r\n" + 
				"	SELECT \r\n" + 
				"		ltc.*, \r\n" + 
				"		llm.location_id \r\n" + 
				"	FROM \r\n" + 
				"		Lane_Type_CTE ltc \r\n" + 
				"	LEFT OUTER JOIN \r\n" + 
				"		location_lane_type_mapping llm \r\n" + 
				"	ON \r\n" + 
				"		ltc.lane_type_id = llm.lane_type_id\r\n" + 
				")\r\n" + 
				"\r\n" + 
				"SELECT \r\n" + 
				"	ltc.rfid_transaction_id, \r\n" + 
				"	ltc.tag_id,ltc.boom_up_request_source, \r\n" + 
				"	ltc.ack_sent_time, \r\n" + 
				"	ltc.hht_boomup_request_recd_time, \r\n" + 
				"	ltc.rfid_boomup_request_recd_time, \r\n" + 
				"	ltc.lane_name, \r\n" + 
				"	ltc.lane_type_name, \r\n" + 
				"	lm.location_name \r\n" + 
				"FROM \r\n" + 
				"	Location_Type_CTE \r\n" + 
				"AS \r\n" + 
				"	ltc \r\n" + 
				"LEFT OUTER JOIN \r\n" + 
				"	location_master lm \r\n" + 
				"ON \r\n" + 
				"	ltc.location_id = lm.location_id";
		
		if(QueryConditions != null && QueryConditions.length() > 0)
			DBQuery += QueryConditions;
		
		Style HeaderStyle = new Style();
		HeaderStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		HeaderStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		HeaderStyle.setBackgroundColor(Color.GRAY);
		HeaderStyle.setTransparency(Transparency.OPAQUE);
		HeaderStyle.setTransparent(false);
		Font HeaderFont = new Font();
		HeaderFont.setBold(true);
		HeaderFont.setFontName(Font.ARIAL_BIG.getFontName());
		HeaderFont.setFontSize(16.0f);
		HeaderStyle.setFont(HeaderFont);
		
		Style ContentStyle = new Style();
		ContentStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		ContentStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		ContentStyle.setBackgroundColor(Color.LIGHT_GRAY);
		ContentStyle.setTransparency(Transparency.OPAQUE);
		ContentStyle.setTransparent(false);
		Font ContentFont = new Font();
		ContentFont.setFontName(Font.ARIAL_SMALL.getFontName());
		ContentFont.setFontSize(13.0f);
		ContentStyle.setFont(ContentFont);
		
		AbstractColumn TagIDCol = ColumnBuilder
								.getNew()
								.setColumnProperty("tag_id", Integer.class.getName())
								.setTitle("RFID Tag ID")
								.setWidth(100)
								.build();
		
		AbstractColumn ReqSrcID = ColumnBuilder
				.getNew()
				.setColumnProperty("boom_up_request_source", Integer.class.getName())
				.setTitle("Boom Up Request Source")
				.setWidth(100)
				.build();
		
		AbstractColumn ReqSentTime = ColumnBuilder
									.getNew()
									.setColumnProperty("ack_sent_time", Date.class.getName())
									.setTitle("Tag Request Sent To Server Time")
									.setWidth(150)
									.build();
		
		AbstractColumn HHTBoomUpReqReceivedTime = ColumnBuilder
				.getNew()
				.setColumnProperty("hht_boomup_request_recd_time", Date.class.getName())
				.setTitle("HHT Boom Up Req Receive Time")
				.setWidth(150)
				.build();

		AbstractColumn RFIDBoomUpReqReceivedTime = ColumnBuilder
				.getNew()
				.setColumnProperty("rfid_boomup_request_recd_time", Date.class.getName())
				.setTitle("RFID Boom Up Req Receive Time")
				.setWidth(150)
				.build();
		
		AbstractColumn Lane = ColumnBuilder
				.getNew()
				.setColumnProperty("lane_name", String.class.getName())
				.setTitle("Lane")
				.setWidth(150)
				.build();
		
		AbstractColumn Location = ColumnBuilder
				.getNew()
				.setColumnProperty("location_name", String.class.getName())
				.setTitle("Location")
				.setWidth(150)
				.build();
		
		AbstractColumn Lane_Type_Name = ColumnBuilder
				.getNew()
				.setColumnProperty("lane_type_name", String.class.getName())
				.setTitle("Lane Type")
				.setWidth(150)
				.build();
		
		FastReportBuilder frb = (FastReportBuilder) new FastReportBuilder()
				.addColumn(TagIDCol)
				.addColumn(ReqSrcID)
				.addColumn(ReqSentTime)
				.addColumn(HHTBoomUpReqReceivedTime)
				.addColumn(RFIDBoomUpReqReceivedTime)
				.addColumn(Lane)
				.addColumn(Lane_Type_Name)
				.addColumn(Location)
				.setTitleStyle(HeaderStyle)
				.setDefaultStyles(null, null, HeaderStyle, ContentStyle)
				.setQuery(DBQuery, DJConstants.QUERY_LANGUAGE_SQL)
				.setUseFullPageWidth(false);
		
		return frb.build();
	}
}
