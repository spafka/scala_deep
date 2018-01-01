/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;


public class RpcClient {

	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();

		//代理类，动态代理
		DemoService proxy =  RPC.getProxy(DemoService.class, DemoService.versionID,
				new InetSocketAddress(conf.get("client.ip.name"), conf.getInt("name.port", 8888)), conf, 1000);

		long l = System.currentTimeMillis();

			proxy.sum(1,1);


		System.out.println(System.currentTimeMillis()-l);
		RPC.stopProxy(proxy);
	}

}
