program t09;

	function area( r : real; ) : real;
	begin
	    return 3.14 * r * r;
	end;

	function perimetro( r : real; ) : real;
	begin
	    return 3.14 * 2 * r;
	end;

begin
	var
	a : real;
	p : real;
	raio : real;

 	writeln("Informe o raio: ");
 	read(raio);
 	a := area(raio);
 	p := perimetro(raio) + "typo"; { # erro de tipos }
	writeln(a);
	writeln(p);
end.
