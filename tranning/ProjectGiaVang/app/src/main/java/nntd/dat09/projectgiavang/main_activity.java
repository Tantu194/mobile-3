package nntd.dat09.projectgiavang;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.*;

import java.text.SimpleDateFormat;
import java.util.*;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class main_activity extends AppCompatActivity {
    public static Activity myActivity;
    ListView list;
    Activity activity;

    private NotificationManager mNotificationManager;
    private int notificationID = 100;
    private int numMessages = 0;


    // Write a message to the database

    String value;
    Switch sw_canedit;
    ImageView iv_icon1, iv_icon2, iv_icon3, iv_icon4;
    EditText txt_price1, txt_price2, txt_price3, txt_price4;
    TextView txt_price_change1, txt_price_change2, txt_price_change3, txt_price_change4, lb_title, lb_havechange1, lb_havechange2, lb_havechange3, lb_havechange4;
    //myRef.setValue("Hello, World!");
    DatabaseReference myRef;
    FirebaseDatabase database;

    int sizePrice = 4;

    ArrayList<String> price = new ArrayList<>();
    ArrayList<String> priceNotFB = new ArrayList<>();
    String[] price_chage = {
            "tăng 0.04%",
            "giảm 0.04%",
            "tăng 0.1%",
            "giảm 0.022%"
    };
    Integer[] imageId = {
            R.drawable.vang48,
            R.drawable.vang19,
            R.drawable.euro,
            R.drawable.dollar

    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        lb_title = (TextView) findViewById(R.id.lb_title);
        lb_title.setText("THÔNG BÁO GIÁ VÀNG VÀ NGOẠI TỆ NGÀY " + getCurrentDate());

        txt_price1 = (EditText) findViewById(R.id.txt_price1);
        txt_price_change1 = (TextView) findViewById(R.id.txt_price_change1);
        iv_icon1 = (ImageView) findViewById(R.id.iv_icon1);
        lb_havechange1 = (TextView)findViewById(R.id.lb_havechange1) ;

        txt_price2 = (EditText) findViewById(R.id.txt_price2);
        txt_price_change2 = (TextView) findViewById(R.id.txt_price_change2);
        iv_icon2 = (ImageView) findViewById(R.id.iv_icon2);
        lb_havechange2 = (TextView)findViewById(R.id.lb_havechange2) ;

        txt_price3 = (EditText) findViewById(R.id.txt_price3);
        txt_price_change3 = (TextView) findViewById(R.id.txt_price_change3);
        iv_icon3 = (ImageView) findViewById(R.id.iv_icon3);
        lb_havechange3 = (TextView)findViewById(R.id.lb_havechange3) ;

        txt_price4 = (EditText) findViewById(R.id.txt_price4);
        txt_price_change4 = (TextView) findViewById(R.id.txt_price_change4);
        iv_icon4 = (ImageView) findViewById(R.id.iv_icon4);
        lb_havechange4 = (TextView)findViewById(R.id.lb_havechange4) ;


        //txt_price1.setText(price[0]);
        txt_price_change1.setText(price_chage[0]);
        iv_icon1.setImageResource(imageId[0]);

        //txt_price2.setText(price[1]);
        txt_price_change2.setText(price_chage[1]);
        iv_icon2.setImageResource(imageId[1]);

        //txt_price3.setText(price[2]);
        txt_price_change3.setText(price_chage[2]);
        iv_icon3.setImageResource(imageId[2]);

        //txt_price4.setText(price[3]);
        txt_price_change4.setText(price_chage[3]);
        iv_icon4.setImageResource(imageId[3]);
        myRef = database.getInstance().getReference();

        sw_canedit = (Switch) findViewById(R.id.sw_canedit);
        sw_canedit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if (on) {
                    priceNotFB.add(txt_price1.getText().toString());
                    priceNotFB.add(txt_price2.getText().toString());
                    priceNotFB.add(txt_price3.getText().toString());
                    priceNotFB.add(txt_price4.getText().toString());

                    Toast.makeText(getApplicationContext(), "Bạn có thể thay đổi các tỉ giá", Toast.LENGTH_LONG).show();

                    txt_price1.setEnabled(true);
                    txt_price2.setEnabled(true);
                    txt_price3.setEnabled(true);
                    txt_price4.setEnabled(true);
                } else {

                    double settext = 0.0;
                    settext = change_price(unformat(priceNotFB.get(0).toString()), unformat(txt_price1.getText().toString()));
                    if (settext > 0) {
                        txt_price_change1.setText("Tăng " + settext + "%");
                        txt_price_change1.setTextColor(getResources().getColor(R.color.colorGreen));

                        lb_havechange1.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange1.setVisibility(View.VISIBLE);
                        lb_havechange1.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange1.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);

                    } else if (settext < 0) {
                        txt_price_change1.setText("Giảm " + (settext * -1) + "%");
                        txt_price_change1.setTextColor(getResources().getColor(R.color.colorRed));

                        lb_havechange1.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange1.setVisibility(View.VISIBLE);
                        lb_havechange1.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange1.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    }

                    settext = change_price(unformat(priceNotFB.get(1).toString()), unformat(txt_price2.getText().toString()));
                    if (settext > 0) {
                        txt_price_change2.setText("Tăng " + settext + "%");
                        txt_price_change2.setTextColor(getResources().getColor(R.color.colorGreen));
                        lb_havechange2.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange2.setVisibility(View.VISIBLE);
                        lb_havechange2.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange1.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    } else if (settext < 0) {
                        txt_price_change2.setText("Giảm " + (settext * -1) + "%");
                        txt_price_change2.setTextColor(getResources().getColor(R.color.colorRed));
                        lb_havechange2.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange2.setVisibility(View.VISIBLE);
                        lb_havechange2.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange1.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    }

                    settext = change_price(unformat(priceNotFB.get(2).toString()), unformat(txt_price3.getText().toString()));
                    if (settext > 0) {
                        txt_price_change3.setText("Tăng " + settext + "%");
                        txt_price_change3.setTextColor(getResources().getColor(R.color.colorGreen));
                        lb_havechange3.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange3.setVisibility(View.VISIBLE);
                        lb_havechange3.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange1.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    } else if (settext < 0) {
                        txt_price_change3.setText("Giảm " + (settext * -1) + "%");
                        txt_price_change3.setTextColor(getResources().getColor(R.color.colorRed));
                        lb_havechange3.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange3.setVisibility(View.VISIBLE);
                        lb_havechange3.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange1.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    }
                    settext = change_price(unformat(priceNotFB.get(3).toString()), unformat(txt_price4.getText().toString()));
                    if (settext > 0) {
                        txt_price_change4.setText("Tăng " + settext + "%");
                        txt_price_change4.setTextColor(getResources().getColor(R.color.colorGreen));
                        lb_havechange4.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange4.setVisibility(View.VISIBLE);
                        lb_havechange4.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange1.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    } else if (settext < 0) {
                        txt_price_change4.setText("Giảm " + (settext * -1) + "%");
                        txt_price_change4.setTextColor(getResources().getColor(R.color.colorRed));
                        lb_havechange4.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange4.setVisibility(View.VISIBLE);
                        lb_havechange4.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange1.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    }

                    priceNotFB.removeAll(price);

                    myRef.child("18k").setValue(txt_price1.getText().toString());
                    myRef.child("24k").setValue(txt_price2.getText().toString());
                    myRef.child("AUD").setValue(txt_price3.getText().toString());
                    myRef.child("USD").setValue(txt_price4.getText().toString());


                    Toast.makeText(getApplicationContext(), "Đã lưu tỉ giá thành công", Toast.LENGTH_LONG).show();
                    txt_price1.setEnabled(false);
                    txt_price2.setEnabled(false);
                    txt_price3.setEnabled(false);
                    txt_price4.setEnabled(false);
                }
            }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                value = dataSnapshot.child("18k").getValue(String.class);


                if (price.size() != 0) {
                    if (checkExet(value) == false) {


                        txt_price1.setText(value);
                        displayNotification("Vàng 18k", 1);
                        price.add(value);


                        double settext = change_price(unformat(price.get(0).toString()), unformat(txt_price1.getText().toString()));
                        if (settext > 0) {
                            txt_price_change1.setText("Tăng " + settext + "%");
                            txt_price_change1.setTextColor(getResources().getColor(R.color.colorGreen));
                            lb_havechange1.setText("Đã có sự thay đổi tỉ giá ");
                            lb_havechange1.setVisibility(View.VISIBLE);
                            lb_havechange1.postDelayed(new Runnable() {
                                public void run() {
                                    lb_havechange1.setVisibility(View.INVISIBLE);
                                }
                            }, 5000);
                        } else if (settext < 0) {
                            txt_price_change1.setText("Giảm " + (settext * -1) + "%");
                            txt_price_change1.setTextColor(getResources().getColor(R.color.colorRed));
                            lb_havechange1.setText("Đã có sự thay đổi tỉ giá ");
                            lb_havechange1.setVisibility(View.VISIBLE);
                            lb_havechange1.postDelayed(new Runnable() {
                                public void run() {
                                    lb_havechange1.setVisibility(View.INVISIBLE);
                                }
                            }, 5000);
                        }

                    }
                } else {

                    txt_price1.setText(value);
                    displayNotification("Vàng 18k", 1);
                    price.add(value);


                    double settext = change_price(unformat(txt_price1.getText().toString()), unformat(value));
                    if (settext > 0) {
                        txt_price_change1.setText("Tăng " + settext + "%");
                        txt_price_change1.setTextColor(getResources().getColor(R.color.colorGreen));
                        lb_havechange1.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange1.setVisibility(View.VISIBLE);
                        lb_havechange1.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange1.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    } else if (settext < 0) {
                        txt_price_change1.setText("Giảm " + (settext * -1) + "%");
                        txt_price_change1.setTextColor(getResources().getColor(R.color.colorRed));
                        lb_havechange1.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange1.setVisibility(View.VISIBLE);
                        lb_havechange1.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange1.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    }




                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value = dataSnapshot.child("24k").getValue(String.class);

                if (price.size() != 0) {
                    if (checkExet(value) == false) {
                        double settext = change_price(unformat(txt_price2.getText().toString()), unformat(value));
                        if (settext > 0) {
                            txt_price_change2.setText("Tăng " + settext + "%");
                            txt_price_change2.setTextColor(getResources().getColor(R.color.colorGreen));
                            lb_havechange2.setText("Đã có sự thay đổi tỉ giá ");
                            lb_havechange2.setVisibility(View.VISIBLE);
                            lb_havechange2.postDelayed(new Runnable() {
                                public void run() {
                                    lb_havechange2.setVisibility(View.INVISIBLE);
                                }
                            }, 5000);
                        } else if (settext < 0) {
                            txt_price_change2.setText("Giảm " + (settext * -1) + "%");
                            txt_price_change2.setTextColor(getResources().getColor(R.color.colorRed));
                            lb_havechange2.setText("Đã có sự thay đổi tỉ giá ");
                            lb_havechange2.setVisibility(View.VISIBLE);
                            lb_havechange2.postDelayed(new Runnable() {
                                public void run() {
                                    lb_havechange2.setVisibility(View.INVISIBLE);
                                }
                            }, 5000);
                        }

                        txt_price2.setText(value);
                        //Log.d("test2",value);
                        displayNotification("Vàng 24k", 2);
                        price.add(value);
                    }
                } else {
                    double settext = change_price(unformat(txt_price2.getText().toString()), unformat(value));
                    if (settext > 0) {
                        txt_price_change2.setText("Tăng " + settext + "%");
                        txt_price_change2.setTextColor(getResources().getColor(R.color.colorGreen));
                        lb_havechange2.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange2.setVisibility(View.VISIBLE);
                        lb_havechange2.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange2.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    } else if (settext < 0) {
                        txt_price_change2.setText("Giảm " + (settext * -1) + "%");
                        txt_price_change2.setTextColor(getResources().getColor(R.color.colorRed));
                        lb_havechange2.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange2.setVisibility(View.VISIBLE);
                        lb_havechange2.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange2.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    }

                    txt_price2.setText(value);
                    //Log.d("test2",value);
                    displayNotification("Vàng 24k", 2);
                    price.add(value);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value = dataSnapshot.child("AUD").getValue(String.class);


                if (price.size() != 0) {
                    if (checkExet(value) == false) {
                        double settext = change_price(unformat(txt_price3.getText().toString()), unformat(value));
                        if (settext > 0) {
                            txt_price_change3.setText("Tăng " + settext + "%");
                            txt_price_change3.setTextColor(getResources().getColor(R.color.colorGreen));
                            lb_havechange3.setText("Đã có sự thay đổi tỉ giá ");
                            lb_havechange3.setVisibility(View.VISIBLE);
                            lb_havechange3.postDelayed(new Runnable() {
                                public void run() {
                                    lb_havechange3.setVisibility(View.INVISIBLE);
                                }
                            }, 5000);
                        } else if (settext < 0) {
                            txt_price_change3.setText("Giảm " + (settext * -1) + "%");
                            txt_price_change3.setTextColor(getResources().getColor(R.color.colorRed));
                            lb_havechange3.setText("Đã có sự thay đổi tỉ giá ");
                            lb_havechange3.setVisibility(View.VISIBLE);
                            lb_havechange3.postDelayed(new Runnable() {
                                public void run() {
                                    lb_havechange3.setVisibility(View.INVISIBLE);
                                }
                            }, 5000);
                        }

                        txt_price3.setText(value);
                        displayNotification("Đồng Dollar Úc", 3);
                        price.add(value);


                    }
                } else {
                    double settext = change_price(unformat(txt_price3.getText().toString()), unformat(value));
                    if (settext > 0) {
                        txt_price_change3.setText("Tăng " + settext + "%");
                        txt_price_change3.setTextColor(getResources().getColor(R.color.colorGreen));
                        lb_havechange3.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange3.setVisibility(View.VISIBLE);
                        lb_havechange3.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange3.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    } else if (settext < 0) {
                        txt_price_change3.setText("Giảm " + (settext * -1) + "%");
                        txt_price_change3.setTextColor(getResources().getColor(R.color.colorRed));
                        lb_havechange3.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange3.setVisibility(View.VISIBLE);
                        lb_havechange3.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange3.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    }

                    txt_price3.setText(value);
                    displayNotification("Đồng Dollar Úc", 3);
                    price.add(value);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value = dataSnapshot.child("USD").getValue(String.class);


                if (price.size() != 0) {
                    if (checkExet(value) == false) {
                        double settext = change_price(unformat(txt_price4.getText().toString()), unformat(value));
                        if (settext > 0) {
                            txt_price_change4.setText("Tăng " + settext + "%");
                            txt_price_change4.setTextColor(getResources().getColor(R.color.colorGreen));
                            lb_havechange4.setText("Đã có sự thay đổi tỉ giá ");
                            lb_havechange4.setVisibility(View.VISIBLE);
                            lb_havechange4.postDelayed(new Runnable() {
                                public void run() {
                                    lb_havechange4.setVisibility(View.INVISIBLE);
                                }
                            }, 5000);
                        } else if (settext < 0) {
                            txt_price_change4.setText("Giảm " + (settext * -1) + "%");
                            txt_price_change4.setTextColor(getResources().getColor(R.color.colorRed));
                            lb_havechange4.setText("Đã có sự thay đổi tỉ giá ");
                            lb_havechange4.setVisibility(View.VISIBLE);
                            lb_havechange4.postDelayed(new Runnable() {
                                public void run() {
                                    lb_havechange4.setVisibility(View.INVISIBLE);
                                }
                            }, 5000);
                        }

                        txt_price4.setText(value);
                        displayNotification("Đồng Dollar Mỹ", 4);
                        price.add(value);


                    }
                } else {
                    double settext = change_price(unformat(txt_price4.getText().toString()), unformat(value));
                    if (settext > 0) {
                        txt_price_change4.setText("Tăng " + settext + "%");
                        txt_price_change4.setTextColor(getResources().getColor(R.color.colorGreen));
                        lb_havechange4.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange4.setVisibility(View.VISIBLE);
                        lb_havechange4.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange4.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    } else if (settext < 0) {
                        txt_price_change4.setText("Giảm " + (settext * -1) + "%");
                        txt_price_change4.setTextColor(getResources().getColor(R.color.colorRed));
                        lb_havechange4.setText("Đã có sự thay đổi tỉ giá ");
                        lb_havechange4.setVisibility(View.VISIBLE);
                        lb_havechange4.postDelayed(new Runnable() {
                            public void run() {
                                lb_havechange4.setVisibility(View.INVISIBLE);
                            }
                        }, 5000);
                    }

                    txt_price4.setText(value);
                    displayNotification("Đồng Dollar Mỹ", 4);
                    price.add(value);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    private double change_price(String old, String newP) {
        Double oldP = Double.parseDouble(old);
        Double newPrice = Double.parseDouble(newP);
        Double kq = ((newPrice - oldP) * 100) / oldP;
        DecimalFormat df = new DecimalFormat("#.##");
        kq = Double.valueOf(df.format(kq));
        return kq;
    }

    private String format(String txt) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###", symbols);
        String prezzo = decimalFormat.format(Integer.parseInt(txt.toString()));
        return prezzo;
    }

    private String unformat(String txt) {
        txt = txt.replace(",", "");
        //double re = Double.parseDouble(txt);
        //return re;
        return txt;
    }

    protected void displayNotification(String type, int ID) {
        /* Sử dụng dịch vụ */
        Notification.Builder mBuilder = new Notification.Builder(this);

        mBuilder.setContentTitle("Tin nhắn mới");
        mBuilder.setContentText("Đã có thay đổi tỉ giá "+type);
        mBuilder.setTicker("Thông báo!");
        mBuilder.setSmallIcon(R.drawable.ic_swap_vertical_circle_black_18dp);

		/* tăng số thông báo */
        mBuilder.setNumber(++numMessages);

		/* Tạo đối tượng chỉ đến activity sẽ mở khi chọn thông báo */
        Intent resultIntent = new Intent(this, NotificationView.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationView.class);

        // kèm dữ liệu theo activity để xử lý
        resultIntent.putExtra("events",
                new String[] { "Tỉ giá "+type+" đã thay đổi vào ngày hôm nay!" });
        resultIntent.putExtra("id", ID);

		/* Đăng ký activity được gọi khi chọn thông báo */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		/* cập nhật thông báo */
        mNotificationManager.notify(ID, mBuilder.build());
    }

    protected void cancelNotification() {
        mNotificationManager.cancel(notificationID);
    }

    protected String getCurrentDate() {
        String pattern = "dd-MM-yyyy";
        return new SimpleDateFormat(pattern).format(new Date());
    }

    protected Boolean checkExet(String txt) {
        for (int i = 0; i < price.size(); i++) {
            if (txt == price.get(i).toString()) {
                return true;
            } else if (i == sizePrice - 1) {
                return false;
            }
        }
        return false;
    }
}
