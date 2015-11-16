program t10;

var
	a : real;
	p : real;
	raio : real;

	function factorial( n : integer; ) : integer;
	var
		i, product : integer;

		function area( r : real; ) : real; { # declaracao de funcoes aninhadas nao eh permitido }
		begin
		    return 3.14 * r * r;
		end;
	
	begin
		product := 1;
		i := 2;
		while i <= n do
			product := product * i;
		endwhile;
	end;

	function perimetro( r : real; ) : real;
	begin
	    return 3.14 * 2 * r;
	end;

begin
 	writeln("Informe o raio: ");
 	read(raio);
 	a := area(raio);
 	p := perimetro(raio);
	writeln(a);
	writeln(p);
end.
