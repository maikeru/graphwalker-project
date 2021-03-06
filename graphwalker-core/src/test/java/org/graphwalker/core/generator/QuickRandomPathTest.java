package org.graphwalker.core.generator;

/*
 * #%L
 * GraphWalker Core
 * %%
 * Copyright (C) 2005 - 2014 GraphWalker
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import org.graphwalker.core.algorithm.AlgorithmException;
import org.graphwalker.core.condition.VertexCoverage;
import org.graphwalker.core.machine.Context;
import org.graphwalker.core.machine.Machine;
import org.graphwalker.core.machine.SimpleMachine;
import org.graphwalker.core.machine.TestExecutionContext;
import org.graphwalker.core.model.Edge;
import org.graphwalker.core.model.Model;
import org.graphwalker.core.model.Vertex;
import org.graphwalker.core.statistics.Profiler;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kristian Karl
 */
public class QuickRandomPathTest {

  private final Vertex source = new Vertex();
  private final Vertex target = new Vertex();
  private final Edge edge = new Edge().setSourceVertex(source).setTargetVertex(target);
  private final Model model = new Model().addEdge(edge);

  @Test
  public void simpleTest() {
    Context context = new TestExecutionContext().setModel(model.build()).setNextElement(source);
    PathGenerator generator = new QuickRandomPath(new VertexCoverage(100));
    context.setPathGenerator(generator);
    Machine machine = new SimpleMachine(context);
    Assert.assertTrue(machine.hasNextStep());
    Assert.assertEquals(machine.getNextStep().getCurrentElement(), source.build());
    Assert.assertEquals(machine.getNextStep().getCurrentElement(), edge.build());
    Assert.assertEquals(machine.getNextStep().getCurrentElement(), target.build());
    Assert.assertFalse(machine.hasNextStep());
  }

  @Test(expected = AlgorithmException.class)
  public void failTest() {
    Context context = new TestExecutionContext().setModel(model.build()).setNextElement(source);
    PathGenerator generator = new QuickRandomPath(new VertexCoverage(100));
    context.setProfiler(new Profiler());
    context.setPathGenerator(generator);
    context.setCurrentElement(source.build());
    Assert.assertEquals(context.getCurrentElement(), source.build());
    Assert.assertEquals(generator.getNextStep().getCurrentElement(), edge.build());
    Assert.assertEquals(generator.getNextStep().getCurrentElement(), target.build());
    context.getPathGenerator().getNextStep(); // should fail
  }

  @Test(expected = NoPathFoundException.class)
  public void emptyModel() {
    Context context = new TestExecutionContext().setModel(new Model().addVertex(source).build()).setNextElement(source);
    PathGenerator generator = new QuickRandomPath(new VertexCoverage(100));
    context.setProfiler(new Profiler());
    context.setPathGenerator(generator);
    context.setCurrentElement(source.build());
    Assert.assertTrue(generator.hasNextStep());
    generator.getNextStep(); // should fail
  }
}
