/**
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
/**
 * Base
 *
 * @fileoverview
 *
 * @suppress {checkTypes|accessControls}
 */

goog.provide('Base');

goog.require('Super');



/**
 * @constructor
 * @extends {Super}
 */
Base = function() {
  Base.base(this, 'constructor');
};
goog.inherits(Base, Super);


Base.prototype.get__text = function() {
  return "A" + Base.superClass_.get__text.apply(this);
};


Base.prototype.set__text = function(value) {
  if (value != Base.superClass_.get__text.apply(this)) {
    Base.superClass_.set__text.apply(this, ["B" + value]);
  }
};


Object.defineProperties(Base.prototype, /** @lends {Base.prototype} */ {
/**
 * @type {string}
 */
text: {
get: Base.prototype.get__text,
set: Base.prototype.set__text}}
);


/**
 * Metadata
 *
 * @type {Object.<string, Array.<Object>>}
 */
Base.prototype.ROYALE_CLASS_INFO = { names: [{ name: 'Base', qName: 'Base', kind: 'class' }] };



/**
 * Reflection
 *
 * @return {Object.<string, Function>}
 */
Base.prototype.ROYALE_REFLECTION_INFO = function () {
  return {
    accessors: function () {
      return {
        'text': { type: 'String', access: 'readwrite', declaredBy: 'Base'}
      };
    },
    methods: function () {
      return {
        'Base': { type: '', declaredBy: 'Base'}
      };
    }
  };
};
/**
 * @const
 * @type {number}
 */
Base.prototype.ROYALE_COMPILE_FLAGS = 9;
