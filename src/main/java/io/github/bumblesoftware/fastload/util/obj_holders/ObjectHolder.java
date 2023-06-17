package io.github.bumblesoftware.fastload.util.obj_holders;

import java.util.List;

public interface ObjectHolder<T> {
   T getHeldObj();

   default boolean equalsAny(List<T> list) {
      for (final var element : list)
         if (element.equals(getHeldObj()))
            return true;
      return false;
   }
}
