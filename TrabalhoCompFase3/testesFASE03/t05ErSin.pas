program t05;

var
    medida_lado: real;
    numlados: integer;
    
    procedure qualpoligono( mlado: real; nlados: integer; );
    var
        mensagem: string;
        calculo: real;
    
    begin
        mensagem := "pentagono";
        if ( nlados = 3 ) then
            calculo := nlados * mlado;
        else 
            if ( nlados = 4 < 5 ) then { # operadores de comparacao nao devem ser utilizados em seguida }
                calculo := mlado * mlado;
            else
                writeln(mensagem);
            endif;
        endif;
        writeln(calculo);
    end;

begin
    read(numlados);
    read(medidalado);
    qualpoligono(medidalado, numlados);
end.
