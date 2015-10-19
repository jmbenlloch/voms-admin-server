/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package search;

import it.infn.voms.admin.search.parser.Parser;
import it.infn.voms.admin.search.parser.QueryParseError;

import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {

  @Test(expected = QueryParseError.class)
  public void testUnquotedValue() {

    String input = "name = bob and surname = 'jones'";
    Parser.newParser().parse(input);
  }

  @Test
  public void testSingleMatch() {

    String input = "name= 'bob'";
    Parser.newParser().parse(input);

  }

  @Test
  public void testConjunction() {

    String input = "name = 'bob' and surname = 'little'";
    Parser.newParser().parse(input);
  }

  @Test
  public void testDisjunction() {

    String input = "name = 'bob' or surname = 'little'";
    Parser.newParser().parse(input);

  }

  @Test
  public void testNestedConjunction() {

    String input = "(name = 'bob' and surname = 'little') or institution = 'ciccio'";
    Parser.newParser().parse(input);

  }

  @Test
  public void testNotLeftBranch() {

    String input = "not suspended  or institution = 'ciccio'";
    Parser.newParser().parse(input);

  }

  @Test
  public void testNotRightBranch() {

    String input = "institution = 'ciccio' and not suspended";
    Parser.newParser().parse(input);

  }

  @Test
  public void testSingleMatchWhitestpace() {

    String input = "name=   'bob   '";
    Parser.newParser().parse(input);

  }

  @Test
  public void testParens() {

    String input = "(name = 'bob' and surname = 'jones') or suspended";
    Parser.newParser().parse(input);
  }

  @Test
  public void testWhitestpaceInMatchString() {

    String input = "name = 'bob' and surname = 'jones  '";
    Parser.newParser().parse(input);

  }

  @Test(expected = QueryParseError.class)
  public void testEmptyInput() {

    Parser.newParser().parse("");

  }

  @Test
  public void testIsOperator() {

    String input = "suspended is false";
    Parser.newParser().parse(input);
  }

  @Test
  public void testImplicitBooleanOperator() {

    String input = "suspended";
    Parser.newParser().parse(input);

  }

  @Test(expected = QueryParseError.class)
  public void testInvalidFieldBooleanOperator() {

    String input = "name is 'andrea'";
    Parser.newParser().parse(input);
  }

  @Test
  public void testNullInput() {

    Assert.assertNull(Parser.newParser().parse(null));
  }

  @Test
  public void testNegatedQuery() {

    String input = "not (name = 'andrea')";
    Parser.newParser().parse(input);
  }

  @Test
  public void testNegatedImplicitBooleanQuery() {

    String input = "not suspended";
    Parser.newParser().parse(input);

  }

  @Test
  public void testIsNullOperator() {

    String input = "institution is null";
    Parser.newParser().parse(input);

  }

  @Test
  public void testIsNotNullOperator() {

    String input = "institution is not null";
    Parser.newParser().parse(input);

  }
  
  @Test
  public void testLikeOperator() {

    String input = "institution like 'NFN'";
    Parser.newParser().parse(input);

  }
  
  @Test
  public void testInGroup() {

    String input = "in_group '/test/t1'";
    Parser.newParser().parse(input);

  }
  
  @Test
  public void testWithRole() {

    String input = "with_role '/test/t1/Role=ciccio'";
    Parser.newParser().parse(input);

  }

}
