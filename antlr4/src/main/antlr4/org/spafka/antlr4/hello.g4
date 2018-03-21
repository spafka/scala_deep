grammar hello;
r :'hello' ID;
ID: [_a-zA-Z0-9]+ ;
WS: [\r\n\t]+ -> skip;
