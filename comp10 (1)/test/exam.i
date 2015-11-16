procedure print( x, y : integer; ch : char )
  var i : integer;
  begin
  write(ch);
  i = 1;
  while i <= 5  do
    begin
    write( x + y );
    i = i + 1;
    end;
  while i <= 50 do
    i = i + 1;
  for i = 1 to 10 do
    x = x + 1;
  for i = 1 to 10 do
     begin
     x = x + 1;
     y = y + 1;
     end;
  end
                
function fatorial( n : integer ) : integer
  begin
  if  n <= 1 
  then
    return 1;
  else
    return n*fatorial(n-1);
  endif;
  end
  
  
procedure main() 
  var n : integer;
  begin
  write( fatorial(5) );
  print(0, 1, 'A');
  for n = 1 to 10 do
    write(n);
  end
  
