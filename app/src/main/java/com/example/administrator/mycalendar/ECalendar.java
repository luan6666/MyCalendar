package com.example.administrator.mycalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by ldx on 2017/11/26.
 */

public class ECalendar extends LinearLayout {
    LayoutInflater inflater;
    GridView calendar_grid;
    ImageView imgNaxt, imgPrve;
    private TextView textDate;
    ECalendarAdapter eCalendarAdapter;
    private Calendar calendar = Calendar.getInstance();
    private Calendar needDay = Calendar.getInstance();
    GestureDetector gestureDetector;
    private OnButtonPress onButtonPress;
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年 MMM");
    public String jumpTag = "";
    public boolean isChoice = false;
    final SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
    Calendar nowCal = Calendar.getInstance();

    public void setNotifyDataSetChanged() {
        eCalendarAdapter.setChange();
    }

    //点击的回调事件
    public void setOnButtonPress(OnButtonPress onButtonPress) {
        this.onButtonPress = onButtonPress;
    }

    public ECalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bindView(context);
        drawGrid();
    }

    public ECalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bindView(context);
        drawGrid();
    }


    public void drawGrid() {

        textDate.setText(sdf.format(calendar.getTime()));
        Calendar cal = (Calendar) calendar.clone();

        final ArrayList<Date> list = new ArrayList<>();
        final ArrayList<Date> formatList = new ArrayList<>();
        int ned = 0;

        cal.set(Calendar.DAY_OF_MONTH, 1);
        ned = cal.get(Calendar.DAY_OF_WEEK) - 1;
        cal.add(Calendar.DAY_OF_MONTH, -ned);
        int maxDay = 6 * 7;
        while (list.size() < maxDay) {
            list.add(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        formatList.addAll(list);
        //点击事件
        calendar_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eCalendarAdapter.setSeclection(position);
                isChoice = false;
                String aa = sdfs.format(formatList.get(position).getTime());
                onButtonPress.onPress(aa + "", aa + "");

            }
        });
        calendar_grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //左右滑动事件,交给gestureDetector处理
        calendar_grid.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        eCalendarAdapter = new ECalendarAdapter(list, getContext());
        calendar_grid.setAdapter(eCalendarAdapter);
        eCalendarAdapter.setChange();

    }

    //初始化界面
    public void bindView(final Context context) {
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calendar_layout, this);
        calendar_grid = (GridView) findViewById(R.id.calendar_grid);
        imgNaxt = (ImageView) findViewById(R.id.img_next);
        imgPrve = (ImageView) findViewById(R.id.img_prve);
        textDate = (TextView) findViewById(R.id.text_date);
        final Calendar timeChoice = Calendar.getInstance();

        //下个月
        imgNaxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                jumpTag = "";
                drawGrid();
                needDay = (Calendar) calendar.clone();
                needDay.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
                String first = sdfs.format(needDay.getTime());
                needDay.set(Calendar.DAY_OF_MONTH, needDay.getActualMaximum(Calendar.DAY_OF_MONTH));
                String last = sdfs.format(needDay.getTime());
                onButtonPress.onPress(first, last);
                eCalendarAdapter.setChange();
            }
        });
        //上个月
        imgPrve.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                jumpTag = "";
                drawGrid();
                needDay = (Calendar) calendar.clone();
                needDay.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
                String first = sdfs.format(needDay.getTime());
                needDay.set(Calendar.DAY_OF_MONTH, needDay.getActualMaximum(Calendar.DAY_OF_MONTH));
                String last = sdfs.format(needDay.getTime());
                onButtonPress.onPress(first, last);
            }
        });
        //处理滑动事件
        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            //根据X轴的移动来进行判断
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() - e2.getX() > 200.00) {
                    calendar.add(Calendar.MONTH, 1);
                    drawGrid();
                } else {
                    calendar.add(Calendar.MONTH, -1);
                    drawGrid();
                }
                return true;
            }
        });
    }

    //自定义点击事件
    public interface OnButtonPress {
        void onPress(String time, String a);
    }
    //画日历的地方
    class ECalendarAdapter extends BaseAdapter {
        List<Date> list = new ArrayList<>();
        Context context;
        int clickTemp = -1;
        SimpleDateFormat sdfssss = new SimpleDateFormat("yyyy-MM-dd");

        public ECalendarAdapter(List<Date> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Date getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Date date = getItem(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.calendar_text_item, null);
                viewHolder = new ViewHolder();
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            int day = date.getDate();
            TextView calendar_text = (TextView) convertView.findViewById(R.id.calendar_text);
            ImageView point = (ImageView) convertView.findViewById(R.id.calendar_point);
            calendar_text.setText(String.valueOf(day));
            boolean isSameDay = false;
            boolean isSameMonth = false;
            boolean isSameYear = false;
            Date now = new Date();
            if (calendar.getTime().getMonth() == date.getMonth()) {
                calendar_text.setTextColor(Color.parseColor("#000000"));
            } else {
                calendar_text.setTextColor(Color.parseColor("#cdcdcd"));
            }
            if (now.getMonth() == date.getMonth()) {
                isSameMonth = true;
            }
            if (now.getDate() == date.getDate()) {
                isSameDay = true;
            }
            if (now.getYear() == date.getYear()) {
                isSameYear = true;
            }
            // 跳转的判断
            //这原先用的是项目的时间选择,等后期再写个补上来,假如没有补上来,记得给我烧纸
            if (isChoice) {
                if (jumpTag != "" && Integer.valueOf(calendar_text.getText().toString()) == (Integer.valueOf(jumpTag.substring(8)))) {
                    int chooseday = Integer.valueOf(calendar_text.getText().toString());
                    if (chooseday > 17 && position > 17) {//根据位置规律判断显示
                        calendar_text.setBackgroundResource(R.mipmap.red_point_circle_1);
                        calendar_text.setTextColor(Color.parseColor("#ffffff"));
                    }
                    if (chooseday < 17 && position < 17) {//根据位置规律判断显示
                        calendar_text.setTextColor(Color.parseColor("#ffffff"));
                        calendar_text.setBackgroundResource(R.mipmap.red_point_circle_1);
                    }
                } else if (isSameDay && isSameMonth && isSameYear) {
                    calendar_text.setBackgroundResource(R.mipmap.blank_point_circle_1);
                } else {
                    calendar_text.setBackgroundResource(0);
                }

            } else {
                if (isSameDay && isSameMonth && isSameYear) {
                    calendar_text.setBackgroundResource(R.mipmap.blank_point_circle_1);
                }
                //判断红圈
                if (clickTemp == position && isSameDay && isSameMonth && isSameYear) {
                    calendar_text.setTextColor(Color.parseColor("#ffffff"));
                    calendar_text.setBackgroundResource(R.mipmap.red_point_circle_1);
                } else if (clickTemp == position) {
                    calendar_text.setTextColor(Color.parseColor("#ffffff"));
                    calendar_text.setBackgroundResource(R.mipmap.red_point_circle_1);
                } else if (isSameDay && isSameMonth && isSameYear) {
                    calendar_text.setBackgroundResource(R.mipmap.blank_point_circle_1);
                } else {
                    calendar_text.setBackgroundResource(0);
                }
            }
                //在这进行数据的处理
                //上面的顺序尽量不要变

            return convertView;
        }

        public void setChange() {

            notifyDataSetChanged();
        }

        //红圈所需
        public void setSeclection(int position) {
            clickTemp = position;
            notifyDataSetChanged();
        }

        class ViewHolder {
            public ViewHolder() {

            }
        }
    }


}
