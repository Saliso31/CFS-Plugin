package com.saliso.cfs;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import static com.saliso.cfs.psi.CfsElementTypes.*;

%%
%class _CfsLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{ return;
%eof}

WS    = [ \t\r\n\f]+
ID    = [A-Za-z_][A-Za-z0-9_]*
DEC   = [0-9][0-9_]*
BLOCK = "/*" ( [^*] | "*"+ [^*/] )* ( "*"+ "/" )?

%%
{WS}                                    { return TokenType.WHITE_SPACE; }
"///"[^\r\n]*                           { return DOC_COMMENT; }
"//"[^\r\n]*                            { return LINE_COMMENT; }
{BLOCK}                                 { return BLOCK_COMMENT; }
\"([^\"\\]|\\[^])*\"?                   { return STRING; }
'([^'\\\r\n]|\\.)'                      { return STRING; }
0x[0-9a-fA-F_]+ | 0b[01_]+ | 0o[0-7_]+ { return NUMBER; }
{DEC}(\.{DEC})?([eE][+-]?{DEC})?        { return NUMBER; }

"pub"        { return KW_PUB; }
"fn"         { return KW_FN; }
"let"        { return KW_LET; }
"var"        { return KW_VAR; }
"struct"     { return KW_STRUCT; }
"impl"       { return KW_IMPL; }
"interface"  { return KW_INTERFACE; }
"for"        { return KW_FOR; }
"in"         { return KW_IN; }
"if"         { return KW_IF; }
"else"       { return KW_ELSE; }
"while"      { return KW_WHILE; }
"loop"       { return KW_LOOP; }
"return"     { return KW_RETURN; }
"break"      { return KW_BREAK; }
"continue"   { return KW_CONTINUE; }
"self"       { return KW_SELF; }
"true"       { return KW_TRUE; }
"false"      { return KW_FALSE; }

"as"         { return KW_AS; }
"as!"        { return KW_AS_BANG; }
"assert"     { return KW_ASSERT; }
"static"     { return KW_STATIC; }
"init"       { return KW_INIT; }

"sizeof"     { return KW_SIZEOF; }
"typeof"     { return KW_TYPEOF; }

{ID}         { return IDENT; }

".@"{DEC}    { return FIELD_OFFSET; }
".#"{DEC}    { return FIELD_INDEX; }
"::"         { return COLONCOLON; }
":"          { return COLON; }
"=="         { return EQEQ; }
"!="         { return NEQ; }
"<="         { return LE; }
">="         { return GE; }
"&&"         { return ANDAND; }
"||"         { return OROR; }
"<"          { return LT; }
">"          { return GT; }
"="          { return EQ; }
"!"          { return BANG; }
"+"          { return PLUS; }
"-"          { return MINUS; }
"*"          { return STAR; }
"/"          { return SLASH; }
"%"          { return PERCENT; }
"&"          { return AMP; }
"{"          { return LBRACE; }
"}"          { return RBRACE; }
"("          { return LPAREN; }
")"          { return RPAREN; }
"["          { return LBRACKET; }
"]"          { return RBRACKET; }
","          { return COMMA; }
";"          { return SEMICOLON; }
"."          { return DOT; }

[^]          { return TokenType.BAD_CHARACTER; }