grammar Day10;

start: expr;

expr: PARENT_OPEN expr* PARENT_CLOSE
    | SQUARE_OPEN expr* SQUARE_CLOSE
    | LESS_THAN expr* GREATER_THAN
    | ACCOLADE_OPEN expr* ACCOLADE_CLOSE;

PARENT_OPEN: '(';
PARENT_CLOSE: ')';
SQUARE_OPEN: '[';
SQUARE_CLOSE: ']';
ACCOLADE_OPEN: '{';
ACCOLADE_CLOSE: '}';
LESS_THAN: '<';
GREATER_THAN: '>';

WS: [ \t]+ -> skip ;

NEWLINE
  : '\r'? '\n'
  | '\r'
  ;


