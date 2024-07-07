#include<iostream>
#include<fstream>

using namespace std;
int main()
{
	double str1[]={18,44};
	double str2[]={26,64};
	double Point_Degree[100][2];
	double D[100];
	int i=0;
	ifstream inputfile;
	inputfile.open("D:/FYP/FYP2/Ammann/A2/Dn.txt");
	double x;
	while(inputfile>>x)
	{
		D[i]=x;
		i++;
	}
	inputfile.close();
	for(int i=0;i<100;i++)
	{
		//cout<<D[i]<<endl;
	}
	//cout<<"stop"<<endl;
	for(int i=0;i<100;i++)
	{
		if (i==0)
		{
		Point_Degree[0][0]=str1[0]+str2[0]-2-D[i];
		Point_Degree[0][1]=str1[1]+str2[1]-2-2*D[i];
		}
		else if (i==1)
		{
		Point_Degree[1][0]=str2[0]+Point_Degree[0][0]-2-D[i];
		Point_Degree[1][1]=str2[1]+Point_Degree[0][1]-2-2*D[i];
		}
		else
		{
		Point_Degree[i][0]=Point_Degree[i-1][0]+Point_Degree[i-2][0]-2-D[i];
		Point_Degree[i][1]=Point_Degree[i-1][1]+Point_Degree[i-2][1]-2-2*D[i];
		}
		
	}
	
	for(i=0;i<100;i++)
	{
		cout<<Point_Degree[i][0]<<"\t"<<Point_Degree[i][1]<<"\t"<<Point_Degree[i][1]/Point_Degree[i][0]<<endl;
	}
	return 0;
}
