package com.example.taxidriver.Ridehistory;

public class HistoryData {

   String date,amt,pic,drop,dis;

    public  HistoryData(String  DATE, String amt, String pic ,String drop,String dis)
    {
        this.amt=amt;
        this.date=DATE;
        this.drop= drop;
        this.pic=pic;
        this.dis=dis;

    }

    public void setDate(String date)
    {
     this.date=date;
    }

    public void setAmt(String amt)
    {
        this.amt=amt;
    }

    public void setDrop(String drop)
    {
        this.drop=drop;
    }

    public void setPic(String pic)
    {
        this.pic=pic;
    }

    public void setDis(String dis)
    {
        this.dis=dis;
    }


    public String getPic()
    {
       return pic;
    }

    public String getdrop()
    {
    return drop;
    }

    public String getAmt()
    {
        return amt;
    }

    public String getDate()
    {
        return date;
    }

    public String getDis()
    {
        return dis;
    }
}
