package org.spafka.sql.streaming.kafka010

object KafkaSourceProvider {
      private val STRATEGY_OPTION_KEYS = Set("subscribe", "subscribepattern", "assign")
      private[kafka010] val STARTING_OFFSETS_OPTION_KEY = "startingoffsets"
      private[kafka010] val ENDING_OFFSETS_OPTION_KEY = "endingoffsets"
      private val FAIL_ON_DATA_LOSS_OPTION_KEY = "failondataloss"
      private val MIN_PARTITIONS_OPTION_KEY = "minpartitions"
}
