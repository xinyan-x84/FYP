A = (1+sqrt(5))/2;
B = (1-sqrt(5))/2;
V=zeros(1,100);
T= zeros(1,100);
D = zeros(1,100);
for n=1:100
    if mod(n,2)~=0
VN = (31/2+69/(2*sqrt(5)))*A^(n-7)+(31/2-69/(2*sqrt(5)))*B^(n-7)+1+2/sqrt(5)*(A^((n+1)/2)-B^((n+1)/2))+1/sqrt(5)*(A^((n-1)/2)-B^((n-1)/2));
TN = (44+98/sqrt(5))*A^(n-7)+(44-98/sqrt(5))*B^(n-7)+4/sqrt(5)*(A^((n+1)/2)-B^((n+1)/2))+2/sqrt(5)*(A^((n-1)/2)-B^((n-1)/2));
    else
VN = (31/2+69/(2*sqrt(5)))*A^(n-7)+(31/2-69/(2*sqrt(5)))*B^(n-7)+1+1/sqrt(5)*(1/2*(A^(n/2)-B^(n/2))+(A^(n/2+1)-B^(n/2+1))+2*(A^(n/2-1)-B^(n/2-1))-1/2*(A^(n/2-3)-B^(n/2-3)));
TN = (44+98/sqrt(5))*A^(n-7)+(44-98/sqrt(5))*B^(n-7)+1/sqrt(5)*((A^(n/2)-B^(n/2))+2*(A^(n/2+1)-B^(n/2+1))+4*(A^(n/2-1)-B^(n/2-1))-(A^(n/2-3)-B^(n/2-3)));
    end
V(n) = VN;
T(n) = TN;
D(n) = TN/VN;
end
