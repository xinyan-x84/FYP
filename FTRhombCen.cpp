#include<iostream>
#include<fstream>
#include<iomanip>

using namespace std;

double arr[35000000][3],FT[35000000][3];
int main()
{
    ifstream inputfile;
    inputfile.open("D:/FYP/FYP2/PenroseTiling/FatAndThin/Fat/Degree/F3.txt");

    double x,y,AverDeg,PointNum=0, TotalLine = 0, PointNum2=0;
    int Count=0,Count2=0;
    
    double maxX=0,maxY=0,minY=10000,minX=10000,KX,KY;
    while(inputfile>>x>>y)
    {
        if(x==0 && y==0)
            break;
        if(x>maxX)
            maxX=x;
        if(y>maxY)
            maxY=y;
        if(y<minY)
            minY=y;
        if(x<minX)
            minX=x;
        arr[Count][0]=x;
        arr[Count][1]=y;
        arr[Count][2]=Count/4+1;
                Count++;
    }
   KX=(1/3)*(maxX-minX); KY=(maxY-minY)*(1/3);

   for(int i=0;i<Count;i+=4)
   {
       for(int j=0;j<Count;j+=4)
       {
           if(arr[i][0]==arr[j][0]&&arr[i][1]==arr[j][1]&&
              arr[i+1][0]==arr[j+1][0]&&arr[i+1][1]==arr[j+1][1]&&
              arr[i+2][0]==arr[j+2][0]&&arr[i+2][1]==arr[j+2][1]&&
              arr[i+3][0]==arr[j+3][0]&&arr[i+3][1]==arr[j+3][1]&&
              arr[i][2]!=arr[j][2])
            for(int k=0;k<4;k++)
           {
               arr[j+k][0]=0;
               arr[j+k][1]=0;
               arr[j+k][2]=0;
           }
       }
   }

for(int i=0;i<Count;i++)
{
    for(int j=0;j<=Count2;j++)
{
    if(arr[i][0]==FT[j][0] && arr[i][1] == FT[j][1]) // && j<Count2)
        break;
    if(arr[i][0]==0 && arr[i][1]==0 && arr[i][2]==0)
        break;
    if(j==Count2 && arr[i][0]<(maxX-KX) &&arr[i][0]>(minX+KX) && arr[i][1]<(maxY-KY) && arr[i][1]>(minY+KY))
    {
        FT[j][0]=arr[i][0];
        FT[j][1]=arr[i][1];
        Count2++;
        break;
    }
}
}
    for(int i=0; i<Count2; i++)
    {
        for(int j=0; j<Count;j++)
        {
            if(FT[i][0] == arr[j][0] && FT[i][1] == arr[j][1])
                FT[i][2]+=1;
        }
    }

    for(int i=0;i<Count2;i++)
    {
        PointNum++;
        TotalLine += FT[i][2];
    }
    AverDeg=TotalLine/PointNum;
    cout<<"The total line is: "<<TotalLine<<endl;
    cout<<"The total point is: "<<PointNum<<endl;
    cout<<"The average degree is: "<<AverDeg<<endl;
    cout<<"The actual total point is: "<<Count<<endl;
    inputfile.close();
    return 0;
}

