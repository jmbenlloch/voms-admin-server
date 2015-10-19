package it.infn.voms.admin.search.parser;

import static org.parboiled.errors.ErrorUtils.printParseErrors;
import it.infn.voms.admin.search.parser.node.Nodes;
import it.infn.voms.admin.search.parser.node.QNode;

import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.StringVar;

class QueryParserImpl extends BaseParser<QNode> implements QueryParser {

  public static QueryParserImpl buildParser() {

    return Parboiled.createParser(QueryParserImpl.class);
  }

  public QueryParserImpl() {

  }

  public QNode parse(String input) {

    if (input == null) {
      return null;
    }

    ParsingResult<QNode> result = new ReportingParseRunner<QNode>(InputLine())
      .run(input.trim());

    if (result.hasErrors()) {

      String parseErrors = printParseErrors(result.parseErrors);
      throw new QueryParseError(parseErrors);
    }

    System.out.println(ParseTreeUtils.printNodeTree(result));

    return result.resultValue;
  }

  Rule InputLine() {

    return Sequence(Query(), EOI);
  }

  Rule Query() {

    return Sequence(MatchGroup(), RightMatchGroup());
  }

  Rule Negation() {

    return Sequence(
      trailingWhitespaceStringLiteral(BooleanOperator.NOT.getRepr()), Query(),
      push(Nodes.negate(pop())));

  }

  Rule Conjuction() {

    return Sequence(WhiteSpace(),
      trailingWhitespaceStringLiteral(BooleanOperator.AND.getRepr()), Query(),
      push(Nodes.conjunction(pop(1), pop())));
  }

  Rule Disjunction() {

    return Sequence(WhiteSpace(),
      trailingWhitespaceStringLiteral(BooleanOperator.OR.getRepr()), Query(),
      push(Nodes.disjunction(pop(1), pop())));
  }

  Rule RightMatchGroup() {

    return ZeroOrMore(FirstOf(Conjuction(), Disjunction()));

  }

  Rule MatchGroup() {

    return FirstOf(Parens(), Negation(), MatchRule());

  }

  Rule IsNullMatchRule() {

    StringVar field = new StringVar();
    return Sequence(StringMatchField(), field.set(match()),
      trailingWhitespaceStringLiteral("is null"),
      push(Nodes.nullMatch(field.get())));

  }

  Rule IsNotNullMatchRule() {

    StringVar field = new StringVar();
    return Sequence(StringMatchField(), field.set(match()),
      trailingWhitespaceStringLiteral("is not null"),
      push(Nodes.notNullMatch(field.get())));

  }

  Rule StringMatchRule() {

    StringVar field = new StringVar();
    StringVar matchOp = new StringVar();
    StringVar value = new StringVar();

    return Sequence(StringMatchField(), field.set(match()),
      StringMatchOperator(), matchOp.set(match()), QuotedStringValue(),
      value.set(match()),
      push(Nodes.fieldMatch(field.get(), matchOp.get(), value.get())));

  }

  Rule ImplicitBooleanMatchRule() {

    StringVar field = new StringVar();
    return Sequence(BooleanMatchField(), field.set(match()),
      push(Nodes.statusMatch(field.get(), Boolean.TRUE)));

  }

  Rule BooleanMatchRule() {

    StringVar field = new StringVar();
    StringVar value = new StringVar();

    return Sequence(BooleanMatchField(), field.set(match()), IsMatchOperator(),
      BooleanValue(), value.set(match()),
      push(Nodes.statusMatch(field.get(), Boolean.parseBoolean(value.get()))));

  }

  Rule MatchRule() {

    return FirstOf(StringMatchRule(), BooleanMatchRule(), IsNullMatchRule(),
      AttributeMatchRule(), IsNotNullMatchRule(), ImplicitBooleanMatchRule());

  }

  Rule AttributeMatchRule() {

    StringVar attributeMatch = new StringVar();
    StringVar value = new StringVar();

    return Sequence(AttributeMatchField(), attributeMatch.set(match()),
      QuotedStringValue(), value.set(match()),
      push(Nodes.attributeExpressionMatch(attributeMatch.get(), value.get())));
  }
  
  

  Rule Parens() {

    return Sequence("( ", Query(), ") ");
  }

  Rule StringMatchField() {

    return FirstOf(trailingWhitespaceStringLiteral(FieldName.NAME.getRepr()),
      trailingWhitespaceStringLiteral(FieldName.ID.getRepr()),
      trailingWhitespaceStringLiteral(FieldName.SURNAME.getRepr()),
      trailingWhitespaceStringLiteral(FieldName.ADDRESS.getRepr()),
      trailingWhitespaceStringLiteral(FieldName.INSTITUTION.getRepr()),
      trailingWhitespaceStringLiteral(FieldName.PHONE_NUMBER.getRepr()),
      trailingWhitespaceStringLiteral(FieldName.EMAIL_ADDRESS.getRepr()));
  }
  

  Rule BooleanMatchField() {

    return FirstOf(
      trailingWhitespaceStringLiteral(BooleanExpressionTerm.SUSPENDED.getRepr()),
      trailingWhitespaceStringLiteral(BooleanExpressionTerm.MEMBERSHIP_EXPIRED
        .getRepr()),
      trailingWhitespaceStringLiteral(BooleanExpressionTerm.PENDING_AUP_REQUEST
        .getRepr()));
  }

  Rule AttributeMatchField() {

    return FirstOf(
      trailingWhitespaceStringLiteral(AttributeExpressionTerm.IN_GROUP
        .getRepr()),
      trailingWhitespaceStringLiteral(AttributeExpressionTerm.WITH_ROLE
        .getRepr()),
      trailingWhitespaceStringLiteral(AttributeExpressionTerm.WITH_ATTRIBUTE
        .getRepr()));

  }

  Rule BooleanOperator() {

    return FirstOf(trailingWhitespaceStringLiteral(BooleanOperator.AND.repr),
      trailingWhitespaceStringLiteral(BooleanOperator.OR.repr));
  }

  Rule BooleanValue() {

    return FirstOf(trailingWhitespaceStringLiteral("true"),
      trailingWhitespaceStringLiteral("false"));
  }

  Rule StringMatchOperator() {

    return FirstOf(
      trailingWhitespaceStringLiteral(MatchOperator.EQUAL.getRepr()),
      trailingWhitespaceStringLiteral(MatchOperator.NOT_EQUAL.getRepr()),
      trailingWhitespaceStringLiteral(MatchOperator.LIKE.getRepr()));
  }

  Rule IsMatchOperator() {

    return trailingWhitespaceStringLiteral(MatchOperator.IS.getRepr());
  }

  Rule QuotedStringValue() {

    return Sequence('\'',
      ZeroOrMore(FirstOf(Escape(), Sequence(TestNot(AnyOf("'\\")), ANY))), '\'');
  }

  Rule Escape() {

    return fromStringLiteral("\\'");
  }

  Rule WhiteSpace() {

    return ZeroOrMore(AnyOf(" \t\f"));
  }

  protected Rule trailingWhitespaceStringLiteral(String s) {

    return fromStringLiteral(s + " ");
  }

  @Override
  protected Rule fromStringLiteral(java.lang.String string) {

    return string.endsWith(" ") ? Sequence(
      String(string.substring(0, string.length() - 1)), WhiteSpace())
      : String(string);
  }

}
