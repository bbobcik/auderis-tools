/*
 * Copyright 2014 Boleslav Bobcik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.auderis.tools.time.timeout;

final class AlwaysExpiredTimeoutImpl extends AbstractBeanBasedTimeout {

	private static final long serialVersionUID = 2616574701166037412L;
	private static final AlwaysExpiredTimeoutImpl INSTANCE = new AlwaysExpiredTimeoutImpl();

	public static AlwaysExpiredTimeoutImpl getInstance() {
		return INSTANCE;
	}

	@Override
	public boolean isExpired() {
		return true;
	}

	@Override
	public void expireNow() {
		// Silently ignored
	}

	@Override
	public long getTimeout() {
		return 0L;
	}

	@Override
	public Timeout setTimeout(long millis) {
		// Silently ignored
		return this;
	}

	@Override
	public Timeout start() {
		// Silently ignored
		return this;
	}

	@Override
	public Timeout startIfNotRunning() {
		// Silently ignored
		return this;
	}

	@Override
	public Timeout stop() {
		// Silently ignored
		return this;
	}

	@Override
	public Timeout restart() {
		// Silently ignored
		return this;
	}

	@Override
	public boolean isRunning() {
		return true;
	}

	@Override
	public boolean isElapsed() {
		return true;
	}

	@Override
	public boolean restartIfElapsed() {
		return true;
	}

	@Override
	public int getRemainingPercent() {
		return 0;
	}

	@Override
	public Long getRemainingMillis() {
		return 0L;
	}

	private AlwaysExpiredTimeoutImpl() {
		super();
	}

	@Override
	public String toString() {
		return "AlwaysExpiredTimeout";
	}

}
