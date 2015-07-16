package pl.marek1and.myworktime.db.schema;

import pl.marek1and.myworktime.db.structure.View;

public class WorkTimeView extends View {

    public static final String VIEW_NAME = "VW_WORKTIME";

    public WorkTimeView() {
        super(VIEW_NAME);

        String createQuery =
                "CREATE VIEW " + VIEW_NAME + " AS " +
                    "SELECT " +
                    "W._ID AS W_ID, " +
                    "W.STARTTIME, " +
                    "W.ENDTIME, " +
                    "W.TYPE, " +
                    "T._ID AS T_ID, " +
                    "T.NAME, " +
                    "T.ICONPATH, " +
                    "N._ID AS N_ID, " +
                    "N.TITLE, " +
                    "N.NOTE, " +
                    "N.MTIME " +
                "FROM WORKTIME W " +
                "LEFT OUTER JOIN TRANSPORT_MAPPING TM ON W._ID = TM.W_ID " +
                "LEFT OUTER JOIN TRANSPORT T ON T._ID = TM.T_ID " +
                "LEFT OUTER JOIN NOTES N ON W._ID = N.W_ID;";
        setCreateQuery(createQuery);
    }

}
