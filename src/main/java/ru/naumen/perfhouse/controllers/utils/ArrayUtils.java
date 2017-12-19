package ru.naumen.perfhouse.controllers.utils;

import java.util.List;
import java.util.stream.Collectors;

public class ArrayUtils {
    /**
     * Преобразовывает List в строку, которая будет воспринята JS как масив строк
     * @param list Список с данными для преобразования
     * @param <T> Любой тип данных
     * @return Строку вида ['value1', 'value2']
     */
    public static <T> String getArrayString(List<T> list)
    {
        return "['" + list.stream()
                .map(Object::toString)
                .collect(Collectors.joining("','")) + "']";
    }

    /**
     * Преобразовывает два массива в строку, которая будет воспринята JS как массив, в котором каждый элемент - массив
     * из строки и числа
     * @param tList Массив дат
     * @param iList Массив чисел
     * @return Строку вида [['value1', 1], ['value2', 3]]
     */
    public static <T, I> String getTwoValuesArrayString(List<T> tList, I[] iList)
    {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i< Math.min(tList.size(), iList.length); i++)
        {
            if (i != 0)
            {
                result.append(",");
            }
            result.append("['").append(tList.get(i)).append("',").append(iList[i]).append("]");
        }
        return result + "]";
    }
}
