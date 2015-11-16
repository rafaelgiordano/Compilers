program t02;

var
    medidalado: real;
    numlados: integer;
    
    procedure qualpoligono( mlado: real; nlados: integer; );
    var
        mensagem: string;
        calculo: real;
    
    begin
        mensagem := 'pentagono';
        if ( nlados = 3 ) then
            calculo := nlados * mlado;
        else 
            calculo := nlados * mlado;
        endif;
        writeln(calculo);
    end;

begin
    read(numlados);
    read(medidalado);
    qualpoligono(medidalado, numlados) { # falta ; }
end.
