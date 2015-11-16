program t08;

var
	a : real;
	p : real;
	raio : real;

	function area( r : real; ) : real;
	begin
	    return 3.14 * r * r;
	end;

	function perimetro( r : real; ) : real;
	begin
	    return 3.14 * 2 * r;
	end;

begin
 	writeln("Informe o raio: ");
 	read(raio);
 	a := area(raio);
 	area(a); { # chamada a funcao nunca deve ser um statement, pois sempre tem valor de retorno }
 	p := perimetro(raio);
	writeln(a);
	writeln(p);
end.
