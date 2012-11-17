/**
 * Copyright 2012 Niall Gallagher
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
package com.googlecode.cqengine.benchmark.tasks;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.benchmark.BenchmarkTask;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.testutil.Car;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.googlecode.cqengine.query.QueryFactory.equal;

/**
 * @author Niall Gallagher
 */
public class HashIndex_ManufacturerFord implements BenchmarkTask {

    private Collection<Car> collection;
    private IndexedCollection<Car> indexedCollection;

    private final Query<Car> query = equal(Car.MANUFACTURER, "Ford");

    @Override
    public void init(Collection<Car> collection) {
        this.collection = collection;
        this.indexedCollection = CQEngine.copyFrom(collection);
        this.indexedCollection.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));
    }

    @Override
    public int runQueryCountResults_IterationNaive() {
        List<Car> results = new LinkedList<Car>();
        for (Car car : collection) {
            if (car.getManufacturer().equals("Ford")) {
                results.add(car);
            }
        }
        return BenchmarkTaskUtil.countResultsViaIteration(results);
    }

    @Override
    public int runQueryCountResults_IterationOptimized() {
        int count = 0;
        for (Car car : collection) {
            if (car.getManufacturer().equals("Ford")) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int runQueryCountResults_CQEngine() {
        ResultSet<Car> results = indexedCollection.retrieve(query);
        return BenchmarkTaskUtil.countResultsViaIteration(results);
    }

    @Override
    public int runQueryCountResults_CQEngineStatistics() {
        ResultSet<Car> results = indexedCollection.retrieve(query);
        return results.size();
    }
}
