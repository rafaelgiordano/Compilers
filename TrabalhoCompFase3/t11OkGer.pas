program t11;

var
	apple, pear, orange : integer;
	text : string;
	number : real;
	a, b : char;

	procedure writentimes( text: string; n : integer; );
	var
		i : integer;
	
	begin
		i := 1;
		while i <= n do
			write(text);
		endwhile;
	end;

	function factorial( n : integer; ) : integer;
	var
		i, product : integer;
	
	begin
		product := 1;
		i := 2;
		while i <= n do
			product := product * i;
		endwhile;
	end;

begin
	apple := 5;
	a := "A";
	pear := factorial (apple);
	orange := 10;
	number := 4.2;
	text := "Hello, world!";
	write(text);
	read(text);
	writeln(text);
end.
