program t03;

var
	x : integer;
	q : integer;

	procedure proc( n : integer; );
	begin
		writeln(n, q);
		return;
	end;

begin	
	x := 1 and 0;
	q := 5 div 2;
    proc(x);
    writeln(x, q, ); { # falta id }
end.
