package net.stockieslad.magical_utilities.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class BlockHelper {
    public static void registerBlockAndItem(Identifier identifier, Block block, Item item) {
        Registry.register(Registries.BLOCK, identifier, block);
        Registry.register(Registries.ITEM, identifier, item);
    }

    @SafeVarargs
    public static <T> DataPool<T> arrayToPool(T... array) {
        DataPool.Builder<T> builder = DataPool.builder();
        for (T element : array) {
            builder.add(element, 1);
        }
        return builder.build();
    }

    public static List<BlockPos> testSides(BlockPos pos, Predicate<BlockPos> condition) {
        var passed = new LinkedList<>(Arrays.stream(Direction.values()).map(pos::offset).filter(condition).toList());
        if (condition.test(pos))
            passed.add(pos);
        return passed;
    }

    public static double[] rotate(Direction direction, double[] box, double normalisationSize, boolean cartesian) {
        var axis = direction.getAxis();
        var positive = direction.getDirection() == Direction.AxisDirection.POSITIVE;
        var axisOrdinal = (axis.ordinal() + 2) % 3;
        box[1] = positive ? box[1] : -box[1];
        box[4] = positive ? box[4] : -box[4];
        box = new double[] {
                box[(2 + axisOrdinal) % 3],
                box[(1 + axisOrdinal) % 3],
                box[(    axisOrdinal)],
                box[(2 + axisOrdinal) % 3  + 3],
                box[(1 + axisOrdinal) % 3  + 3],
                box[(    axisOrdinal)      + 3],
        };

        if (normalisationSize != 0) {
            if (positive) box = new double[] {
                    cartesian ? box[0] : Math.abs(box[0]),
                    cartesian ? box[1] : Math.abs(box[1]),
                    cartesian ? box[2] : Math.abs(box[2]),
                    cartesian ? box[3] : Math.abs(box[3]),
                    cartesian ? box[4] : Math.abs(box[4]),
                    cartesian ? box[5] : Math.abs(box[5]),
            };
            else box = new double[] {
                    cartesian ? box[3] : normalisationSize - Math.abs(box[3]),
                    cartesian ? box[4] : normalisationSize - Math.abs(box[4]),
                    cartesian ? box[5] : normalisationSize - Math.abs(box[5]),
                    cartesian ? box[0] : normalisationSize - Math.abs(box[0]),
                    cartesian ? box[1] : normalisationSize - Math.abs(box[1]),
                    cartesian ? box[2] : normalisationSize - Math.abs(box[2]),
            };
        }

        return box;
    }

    public static double[] rotate(Direction direction, double[] box) {
        return rotate(direction, box, 0, false);
    }
}
