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
package it.infn.voms.admin.search.parser.node;

public abstract class QNode implements BinTreeNode<QNode>, Visitable {

  private final QNode left, right;
  private QNode parent;

  protected QNode(QNode left, QNode right) {

    if (left != null) {
      left.setParent(this);
    }
    if (right != null) {
      right.setParent(null);
    }

    this.left = left;
    this.right = right;

  }

  protected QNode() {

    this(null, null);
  }

  protected QNode(QNode left) {

    this(left, null);
  }

  @Override
  public QNode left() {

    return left;
  }

  @Override
  public QNode right() {

    return right;
  }

  @Override
  public QNode getParent() {

    return parent;
  }

  protected void setParent(QNode parent) {

    this.parent = parent;
  }

  @Override
  public <V> V accept(QNodeVisitor<V> visitor) {

    throw new IllegalArgumentException();
  }
}
