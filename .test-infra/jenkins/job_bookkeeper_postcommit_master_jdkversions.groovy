/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import common_job_properties

// This job runs the Java postcommit tests cross multiple JDK versions.
matrixJob('bookkeeper_postcommit_master_jdkversions') {
  description('Runs nightly build for bookkeeper in multiple Jdk versions.')

  // Set common parameters.
  common_job_properties.setTopLevelMainJobProperties(delegate)

  // Set JDK versions.
  axes {
    label('label', 'ubuntu')
    jdk('JDK 1.8 (latest)',
        'JDK 1.9 (latest)')
  }

  // Sets that this is a PostCommit job.
  common_job_properties.setPostCommit(
      delegate,
      'H 12 * * *',
      false)

  // Allows triggering this build against pull requests.
  common_job_properties.enablePhraseTriggeringFromPullRequest(
      delegate,
      'JDK Version Test',
      '/test-jdks')

  // Maven build for this job.
  steps {
    maven {
      // Set maven parameters.
      common_job_properties.setMavenConfig(delegate)

      // Maven build project.
      goals('clean apache-rat:check package spotbugs:check')
    }
  }
}
