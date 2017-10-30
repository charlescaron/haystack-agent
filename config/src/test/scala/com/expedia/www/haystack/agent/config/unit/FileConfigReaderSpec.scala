/*
 *  Copyright 2017 Expedia, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */

package com.expedia.www.haystack.agent.config.unit

import com.expedia.www.haystack.agent.config.spi.FileConfigReader
import org.scalatest.{Entry, FunSpec, Matchers}

class FileConfigReaderSpec extends FunSpec with Matchers {

  describe("File ConfigReader spi") {
    it("should read the config file and load the agent config") {
      val reader = new FileConfigReader()
      val agentConfigs = reader.read().getAgentConfigs

      reader.getName shouldEqual "file"
      agentConfigs.size() shouldBe 2

      val spanAgentConfig = agentConfigs.get(0)
      spanAgentConfig.toString shouldEqual "com.expedia.www.haystack.agent.config.AgentConfig@5f049ea1[name=spans,props={key1=value1, port=8080},dispatchers={kinesis={arn=arn-1, queueName=myqueue}}]"
      spanAgentConfig.getName shouldEqual "spans"
      spanAgentConfig.getProps should contain (Entry("key1", "value1"))
      spanAgentConfig.getProps should contain (Entry("port", 8080))

      spanAgentConfig.getDispatchers.size() shouldBe 1
      val kinesisDispatcher = spanAgentConfig.getDispatchers.get("kinesis")
      kinesisDispatcher should contain (Entry("arn", "arn-1"))
      kinesisDispatcher should contain (Entry("queueName", "myqueue"))

      val blobsAgentConfig = agentConfigs.get(1)
      blobsAgentConfig.toString shouldEqual "com.expedia.www.haystack.agent.config.AgentConfig@609e8838[name=blobs,props={key2=value2, port=80},dispatchers={s3={iam=iam-role}}]"
      blobsAgentConfig.getName shouldEqual "blobs"
      blobsAgentConfig.getProps should contain (Entry("key2", "value2"))
      blobsAgentConfig.getProps should contain (Entry("port", 80))

      blobsAgentConfig.getDispatchers.size() shouldBe 1
      val s3Dispatcher = blobsAgentConfig.getDispatchers.get("s3")
      s3Dispatcher should contain (Entry("iam", "iam-role"))

    }
  }
}

