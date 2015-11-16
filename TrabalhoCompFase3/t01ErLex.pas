program t01;

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
 	writeln('Informe o raio: ');
 	read(raio);
 	a := area(raio);
 	p : perimetro(raio); { # simbolo errado de atribuicao }
	writeln(a);
	writeln(p);
end.
