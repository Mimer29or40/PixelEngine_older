package pe.phx2D.util;

import java.util.List;

/**
 * Provides a list of {@link Observable Subjects} contained in this object. Used when creating an {@link EasyScriptParser}.
 */
public interface ObservableList
{
    /**
     * Returns list of Subjects contained in this object, possibly including this object itself.
     *
     * @return the Subjects contained in this object
     */
    List<Observable> getSubjects();
}
