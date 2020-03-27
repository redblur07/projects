package com.gill.reciever;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class CustomLayout extends LinearLayout{
int normal=0xffedebe6;
int blue=0x330000ff;
int yellow=0x33ffff00;
int green=0x3300ff00;
int frozy=0x3300ffff;
int red=0x33ff0000;
int transparent=0x00000000;
int white=0x99e1e8ea;
int d=0xffdee1e5;
int color=transparent;
int Orientation=VERTICAL;
int width=LayoutParams.MATCH_PARENT;
int height=LayoutParams.MATCH_PARENT;
LayoutParams params;
public CustomLayout(Context context){
  super(context);
  createLayout();
}
public CustomLayout(Context context, int o){
  super(context);
  this.Orientation=o;
  createLayout();
}
public CustomLayout(Context context, char c){
  super(context);
  setBgColor(c);
  createLayout();
}
public CustomLayout(Context context, int w, int h){
  super(context);
  this.width=w;
  this.height=h;
}
public CustomLayout(Context context, int o, char c){
  super(context);
  this.Orientation=o;
  setBgColor(c);
  createLayout();
}
public CustomLayout(Context context, int o, int w, int h){
  super(context);
  this.width=w;
  this.height=h;
  this.Orientation=o;
  createLayout();
}
public CustomLayout(Context context, char c, int w, int h){
  super(context);
  setBgColor(c);
  this.width=w;
  this.height=h;
  createLayout();
}
public CustomLayout(Context context, int o, int c, int w, int h){
  super(context);
  this.width=w;
  this.height=h;
  this.Orientation=o;
  this.color=c;
  createLayout();
}

public void setSize(int w, int h){
  this.width=w;
  this.height=h;
  params=new LayoutParams(w, h);
  setLayoutParams(params);
}
public void setBgColor(char c){
  if(c=='B')
  this.color=blue;  
  if(c=='Y')
  this.color=yellow;  
  if(c=='G'){
  this.color=green;
  }  
  if(c=='F'){
  this.color=frozy;
  }
  if(c=='R'){
  this.color=red;
  }  
  if(c=='T'){
  this.color=transparent;
  }
  if(c=='W'){
  this.color=white;
  }
  if(c=='D'){
  this.color=d;
  }
  if(c=='A'){
  this.color=normal;
  }
}
public void createLayout(){
  setSize(width, height);
  setOrientation(Orientation);
  setPadding(1,1,1,1);
  setGravity(1);
  setBackgroundColor(color);
}


}
