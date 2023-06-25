package io.github.bumblesoftware.fastload.updated_code.obj_holders;

import java.util.List;

public interface ObjectHolder<T> {
   T getHeldObj();

   ObjectHolder<T> mutateHeldObj(T newObj);

   default boolean equalsAny(List<T> list) {
      for (final var element : list)
         if (element.equals(getHeldObj()))
            return true;
      return false;
   }

}
