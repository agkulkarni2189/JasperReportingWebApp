package DynamicJasperLibrary;

import java.awt.Color;
import java.sql.Date;
import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
;
public class ReportingUtility {

	public ReportingUtility() {
		super();
	}
		// TODO Auto-generated constructor stub
		
	public DynamicReport buildReport(String LocationID, String LaneTypeID, String LaneID) throws Exception
	{	
		StringBuilder sb = new StringBuilder();
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
		
		String DBQuery = "With"
				+ " LaneTrans_CTE As("
				+ "SELECT "
				+ "tt.tag_id, "
				+ "tt.boom_up_request_source, "
				+ "tt.ack_sent_time, "
				+ "tt.hht_boomup_request_recd_time, "
				+ "tt.rfid_boomup_request_recd_time, "
				+ "lm.lane_id, "
				+ "lm.lane_type_id, "
				+ "lm.lane_name "
				+ "FROM "
				+ "rfid_transaction_table tt "
				+ "LEFT OUTER JOIN "
				+ "lane_master lm "
				+ "ON "
				+ "tt.lane_id=lm.lane_id"
				+ "),"
				+ "LaneLaneType_CTE As("
				+ "SELECT ltc.*, "
				+ "ltm.lane_type_name "
				+ "from LaneTrans_CTE ltc "
				+ "LEFT OUTER JOIN "
				+ "lane_type_master ltm "
				+ "ON "
				+ "ltc.lane_type_id=ltm.lane_type_id"
				+ "),"
				+ "LaneTypeLocation_CTE As("
				+ "SELECT llt.*, "
				+ "llm.location_id "
				+ "FROM "
				+ "LaneLaneType_CTE llt "
				+ "LEFT OUTER JOIN "
				+ "location_lane_type_mapping llm "
				+ "ON "
				+ "llt.lane_type_id=llm.lane_type_id"
				+ ") "
				+ "SELECT "
				+ "ltlc.tag_id, "
				+ "ltlc.boom_up_request_source, "
				+ "ltlc.ack_sent_time, "
				+ "ltlc.hht_boomup_request_recd_time, "
				+ "ltlc.rfid_boomup_request_recd_time, "
				+ "ltlc.lane_name, "
				+ "lm.location_name, "
				+ "ltlc.lane_type_name "
				+ "FROM "
				+ "LaneTypeLocation_CTE ltlc "
				+ "LEFT OUTER JOIN "
				+ "location_master lm "
				+ "ON "
				+ "ltlc.location_id=lm.location_id";
		
		if(LocationID != null && !LocationID.isEmpty())
		{
			if(sb.length() <= 0)
				sb.append(" WHERE ltlc.location_id = "+LocationID);
			else
				sb.append(" AND ltlc.location_ID = "+LocationID);
		}
		
		if(LaneTypeID != null && !LaneTypeID.isEmpty())
		{
			if(sb.length() <= 0)
				sb.append(" WHERE ltlc.lane_type_id = "+LaneTypeID);
			else
				sb.append(" AND ltlc.lane_type_id = "+LaneTypeID);
		}
		
		if(LaneID != null && !LaneID.isEmpty())
		{
			if(sb.length() > 0)
				sb.append(" AND ltlc.lane_id = "+LaneID);
			else
				sb.append(" WHERE ltlc.lane_id = "+LaneID);
		}
		
		if(sb.length() > 0)
			DBQuery += sb.toString();
		
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
				.setQuery(DBQuery, DJConstants.QUERY_LANGUAGE_SQL)
				.setDefaultStyles(null, null, HeaderStyle, ContentStyle)
				.setUseFullPageWidth(false);
		
		return frb.build();
	}
}
