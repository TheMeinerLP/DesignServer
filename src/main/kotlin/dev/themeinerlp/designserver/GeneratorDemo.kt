package dev.themeinerlp.designserver

import net.minestom.server.instance.Chunk
import net.minestom.server.instance.ChunkGenerator
import net.minestom.server.instance.ChunkPopulator
import net.minestom.server.instance.batch.ChunkBatch
import net.minestom.server.instance.block.Block
import net.minestom.server.world.biomes.Biome
import java.util.*


class GeneratorDemo : ChunkGenerator {
    override fun generateChunkData(batch: ChunkBatch, chunkX: Int, chunkZ: Int) {
        for (x in 0 until Chunk.CHUNK_SIZE_X) {
            for (z in 0 until Chunk.CHUNK_SIZE_Z) {
                for (y in 0..39) {
                    batch.setBlock(x, y, z, Block.STONE)
                }
            }
        }
    }

    override fun fillBiomes(biomes: Array<out Biome>, chunkX: Int, chunkZ: Int) {
        Arrays.fill(biomes, Biome.PLAINS)
    }

    override fun getPopulators(): MutableList<ChunkPopulator>? = null
}