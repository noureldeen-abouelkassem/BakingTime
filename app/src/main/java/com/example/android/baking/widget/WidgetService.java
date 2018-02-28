package com.example.android.baking.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.android.baking.provider.Contract.Entry;
import com.example.android.baking.R;


public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(getApplicationContext(), intent);
    }

    class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;
        private int mAppWidgetId;
        private Cursor mCursor;


        public WidgetRemoteViewsFactory(Context context, Intent intent) {

            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }


        private void initCursor() {
            if (mCursor != null) {
                mCursor.close();
            }
            Uri uri = Entry.CONTENT_URI;
            mCursor = mContext.getContentResolver().query(uri, null, null, null, null);
        }

        @Override
        public void onCreate() {
            initCursor();
        }

        @Override
        public void onDataSetChanged() {
            initCursor();
        }

        @Override
        public void onDestroy() {
            mCursor.close();
            mCursor = null;
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {


            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_list_item);

            if (mCursor.getCount() != 0) {
                mCursor.moveToPosition(position);

                remoteViews.setTextViewText(R.id.widget_ingredient_name,
                        mCursor.getString(mCursor.getColumnIndex(Entry.COLUMN_NAME_INGREDIENTS)));

                String measure =
                        mCursor.getString(mCursor.getColumnIndex(Entry.COLUMN_NAME_QUANTITY))
                                + " " +
                                mCursor.getString(mCursor.getColumnIndex(Entry.COLUMN_NAME_MEASURE));

                remoteViews.setTextViewText(R.id.widget_ingredient_measure, measure);
            }

            // Return the remote views object.
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
