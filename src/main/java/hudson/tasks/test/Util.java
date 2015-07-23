/*
 * The MIT License
 * 
 * Copyright (c) 2009, Yahoo!, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.tasks.test;

import hudson.model.Cause;
import hudson.model.Cause.UpstreamCause;

import java.util.List;

/**
 * Common utility functions for this package.
 */
class Util {

    private Util() { /* Utility class */ }

    /**
     * Check if the two set of causes are similar.
     *
     * @return true if the causes are similar otherwise false.
     */
    static final boolean similarCauses(List<Cause> foo, List<Cause> bar) {
        // check same size
        if(foo.size() != bar.size()) {
            return false;
        }

        for (int i = foo.size() -1; i >= 0; i--) {
            Cause fooCause = foo.get(i);
            Cause barCause = bar.get(i);
            // make sure its the same Cause types
            if(!fooCause.getClass().equals(barCause.getClass())) {
                return false;
            }
            // Cause.UpstreamCause should match the same list of upstream Jobs
            if(fooCause instanceof UpstreamCause) {
                UpstreamCause upFooCause = (UpstreamCause) fooCause;
                UpstreamCause upBarCause = (UpstreamCause) barCause;
                String fooProj = upFooCause.getUpstreamProject();
                String barProj = upFooCause.getUpstreamProject();
                if (fooProj == null ? barProj != null : !fooProj.equals(barProj)) {
                    return false;
                }
                if(!similarCauses(upFooCause.getUpstreamCauses(), upBarCause.getUpstreamCauses())) {
                    return false;
                }
            } else if(!fooCause.equals(barCause)) {
                return false;
            }
        }
        return true;
    }
}
