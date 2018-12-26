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
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;;

public class ReportingUtility {

	public ReportingUtility() {
		super();
	}
	// TODO Auto-generated constructor stub

	public DynamicReport buildReport(String LocationID, String LaneTypeID, String LaneID) throws Exception {
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

		StringBuilder DBQuery = new StringBuilder();
		DBQuery.append("WITH Lane_Trans_CTE AS(SELECT tt.rfid_transaction_id, tt.tag_id, tt.boom_up_request_source, tt.ack_sent_time, tt.hht_boomup_request_recd_time, tt.rfid_boomup_request_recd_time, lm.lane_name, lm.lane_id, lm.lane_type_id  FROM rfid_transaction_table tt LEFT OUTER JOIN lane_master lm ON tt.lane_id=lm.lane_id),Lane_Type_CTE AS(SELECT ltc.*, ltm.lane_type_name FROM Lane_Trans_CTE ltc LEFT OUTER JOIN lane_type_master ltm ON ltc.lane_type_id=ltm.lane_type_id), Location_Type_CTE AS(SELECT ltc.*, llm.location_id FROM Lane_Type_CTE ltc LEFT OUTER JOIN location_lane_type_mapping llm ON ltc.lane_type_id = llm.lane_type_id) SELECT ltc.rfid_transaction_id, ltc.tag_id,ltc.boom_up_request_source, ltc.ack_sent_time, ltc.hht_boomup_request_recd_time, ltc.rfid_boomup_request_recd_time, ltc.lane_name, ltc.lane_type_name, lm.location_name FROM Location_Type_CTE AS ltc LEFT OUTER JOIN location_master lm ON ltc.location_id = lm.location_id");

		if (LocationID != null && !LocationID.isEmpty()) {
			if (sb.length() <= 0)
				sb.append(" WHERE ltlc.location_id = " + LocationID);
			else
				sb.append(" AND ltlc.location_id = " + LocationID);
		}

		if (LaneTypeID != null && !LaneTypeID.isEmpty()) {
			if (sb.length() <= 0)
				sb.append(" WHERE ltlc.lane_type_id = " + LaneTypeID);
			else
				sb.append(" AND ltlc.lane_type_id = " + LaneTypeID);
		}

		if (LaneID != null && !LaneID.isEmpty()) {
			if (sb.length() > 0)
				sb.append(" AND ltlc.lane_id = " + LaneID);
			else
				sb.append(" WHERE ltlc.lane_id = " + LaneID);
		}

		if (sb.length() > 0)
			DBQuery.append(sb.toString());

		AbstractColumn TagIDCol = ColumnBuilder.getNew().setColumnProperty("TAG_ID", Integer.class.getName())
				.setTitle("RFID Tag ID").setWidth(100).build();

		AbstractColumn ReqSrcID = ColumnBuilder.getNew()
				.setColumnProperty("BOOM_UP_REQUEST_SOURCE", Integer.class.getName()).setTitle("Boom Up Request Source")
				.setWidth(100).build();

		AbstractColumn ReqSentTime = ColumnBuilder.getNew()
				.setColumnProperty("TAG_REQUEST_SENT_TO_SERVER_TIME", Date.class.getName())
				.setTitle("Tag Request Sent To Server Time").setWidth(150).build();

		AbstractColumn HHTBoomUpReqReceivedTime = ColumnBuilder.getNew()
				.setColumnProperty("HHT_BOOM_UP_REQUEST_RECEIVED_TIME", Date.class.getName())
				.setTitle("HHT Boom Up Req Receive Time").setWidth(150).build();

		AbstractColumn RFIDBoomUpReqReceivedTime = ColumnBuilder.getNew()
				.setColumnProperty("RFID_REQUEST_RECEIVED_TIME", Date.class.getName())
				.setTitle("RFID Boom Up Req Receive Time").setWidth(150).build();

		AbstractColumn Lane = ColumnBuilder.getNew().setColumnProperty("LANE_NAME", String.class.getName())
				.setTitle("Lane").setWidth(150).build();

		AbstractColumn Location = ColumnBuilder.getNew().setColumnProperty("LOCATION_NAME", String.class.getName())
				.setTitle("Location").setWidth(150).build();

		AbstractColumn Lane_Type_Name = ColumnBuilder.getNew()
				.setColumnProperty("LANE_TYPE_NAME", String.class.getName()).setTitle("Lane Type").setWidth(150)
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
				.setQuery(DBQuery.toString(), DJConstants.QUERY_LANGUAGE_SQL)
				.setDefaultStyles(null, null, HeaderStyle, ContentStyle).setUseFullPageWidth(true);

		return frb.build();
	}
}
