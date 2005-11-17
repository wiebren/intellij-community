/*
 * Copyright 2000-2005 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.uiDesigner.compiler;

import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.lw.LwComponent;
import com.intellij.uiDesigner.lw.LwContainer;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: yole
 * Date: 17.11.2005
 * Time: 19:59:51
 * To change this template use File | Settings | File Templates.
 */
public class GridLayoutCodeGenerator extends LayoutCodeGenerator {
  public void generateContainerLayout(final LwComponent lwComponent, final GeneratorAdapter generator, final int componentLocal) {
    if (lwComponent instanceof LwContainer) {
      LwContainer container = (LwContainer) lwComponent;
      if (container.isGrid()) {
        // arg 0: object
        generator.loadLocal(componentLocal);

        // arg 1: layout
        final GridLayoutManager layout = (GridLayoutManager)container.getLayout();

        final Type gridLayoutManagerType = Type.getType(GridLayoutManager.class);
        generator.newInstance(gridLayoutManagerType);
        generator.dup();
        generator.push(layout.getRowCount());
        generator.push(layout.getColumnCount());
        newInsets(generator, layout.getMargin());
        generator.push(layout.getHGap());
        generator.push(layout.getVGap());
        generator.push(layout.isSameSizeHorizontally());
        generator.push(layout.isSameSizeVertically());
        generator.invokeConstructor(gridLayoutManagerType, Method.getMethod("void <init> (int,int,java.awt.Insets,int,int,boolean,boolean)"));

        generator.invokeVirtual(Type.getType(Container.class), Method.getMethod("void setLayout(java.awt.LayoutManager)"));
      }
    }
  }
}
