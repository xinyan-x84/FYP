    D=zeros(1,100);
for N=1:100
 if mod(N,2)==0
    DN=5^(-1/2)*(power((1+sqrt(5))/2,(N+2)/2)-power((1-sqrt(5))/2,(N+2)/2))+5^(-1/2)*(power((1+sqrt(5))/2,(N+6)/2)-power((1-sqrt(5))/2,(N+6)/2))-1;
 else
    DN=5^(-1/2)*(power((1+sqrt(5))/2,(N+5)/2)-power((1-sqrt(5))/2,(N+5)/2))+5^(-1/2)*(power((1+sqrt(5))/2,(N+3)/2)-power((1-sqrt(5))/2,(N+3)/2))-1;
end
disp(DN)
D(N)=DN;
end




