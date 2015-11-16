program t07;

var
	x : integer;
	q : integer;

	procedure proc( n : integer; );
	begin
		writeln(n, q);
		return n; { # procedimentos nao devem retornar expressao }
	end;

begin	
	x := 10;
end.
