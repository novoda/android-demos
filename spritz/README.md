# spritz

_Animate your view pager with Lottie and zero efforts._

--------

Spritz is an Android library to seamlessly trigger a Lottie animation when the user drags a `ViewPager` or changes page. It supports animating 
while swiping and auto-playing when the swipe is complete.

![A taste of spritz](images/a-taste-of-spritz.gif)

## How to use

### Basics

Each step in the `ViewPager` has two possible animations:

- a "swipe away" animation that will be triggered while the user drags from one page to the next or previous one
- an "autoplay" animation segment that plays as soon as the user enters the destination page

When swiping forward, the animation will stop right before auto-playing. When swiping backwards, instead, the animation will stop at the end of the
previous page's autoplay animation, so it doesn't play again since that page is in an "already visited" state.

### Lottie Animation

To learn how to create a Lottie-compatible animation from After Effects, please read 
[our blog post "Whole Lottie Love"](https://www.novoda.com/blog/whole-lottie-love/), which contains basic instructions on how to export an animation
into a Lottie-supported format.

Your animation should have:

- for each page, a "swipe away" animation
- for each page, optionally, an "autoplay" animation segment

These segments must be consecutive to one another, as if the user continuously moves to one page to another.

### Code!

After getting your `LottieAnimationView` and your `ViewPager`, just create a `Spritz` object with the following syntax:

```
Spritz
    .with(lottieAnimationView)
    .withDefaultSwipeAnimationDuration(300, TimeUnit.MILLISECONDS)
    .withDefaultSwipeForwardInterpolator(SWIPE_FORWARD_INTERPOLATOR)
    .withSteps(
            new SpritzStep.Builder()
                    .withAutoPlayDuration(500, TimeUnit.MILLISECONDS)
                    .withSwipeDuration(500, TimeUnit.MILLISECONDS)
                    .build(),
            new SpritzStep.Builder()
                    .withAutoPlayDuration(500, TimeUnit.MILLISECONDS)
                    .withSwipeDuration(500, TimeUnit.MILLISECONDS)
                    .withSwipeBackwardsInterpolator(SWIPE_BACKWARDS_INTERPOLATOR)
                    .build(),
            new SpritzStep.Builder()
                    .withAutoPlayDuration(500, TimeUnit.MILLISECONDS)
                    .build()
    )
    .attachTo(viewPager);
```

## Technical documentation

### Make your own kind of Spritz

Start building your view pager animation by calling:

```
SpritzPage.with(lottieAnimationView)
```

This will set the specific `LottieAnimationView` as the target of your animations. The Lottie view must load the animation separately, as it is not 
in the scope of this library.

After that, you can proceed setting your view pager transitions.

#### `withDefaultSwipeAnimationDuration`

`withDefaultSwipeAnimationDuration(long duration, TimeUnit timeUnit)` allows you to specify how long the swiping animation has to complete if the user 
stops dragging.

#### `withDefaultSwipeForwardInterpolator`

`withDefaultSwipeForwardInterpolator(TimeInterpolator swipeForwardInterpolator)` sets the interpolator to use for all pages to complete the swipe 
animation if the user stops dragging forwards at any point (before starting the autoplay animation, if any). If you don't set this, it will default to
`LinearInterpolator`. You can always override this default for single pages (see [`withSwipeForwardInterpolator`](#withswipeforwardinterpolator)).

#### `withDefaultSwipeBackwardsInterpolator`

`withDefaultSwipeBackwardsInterpolator(TimeInterpolator swipeBackwardsInterpolator)` sets the interpolator to use for all pages to complete the swipe 
animation if the user stops  dragging backwards at any point. If you don't set this, it will default to `LinearInterpolator`. You can always override 
this default for single pages (see [`withSwipeBackwardsInterpolator`](#withswipebackwardsinterpolator)).

#### `withSteps`

`withSteps(SpritzStep... steps)` lets you specify the steps to animate through. The library is made to work with the same number of pages as the ones 
set in the `ViewPager`.

See [Building the steps](#building-the-steps) to learn how to build a list of steps that match your exported animation.

#### `attachTo`

After setting the steps, simply call `attachTo(ViewPager viewPager)` to register all the needed listeners and set everything up. Congratulations! Now
you have a `spritz` object!

#### `startPendingAnimations`

You can call `startPendingAnimations` on your `spritz` object to trigger the autoplay animation for your first step.

### Building the steps

To build a step, use the `SpritzStep.Builder` class, configure it as explained in the following sections, then build it with `.build()`.

#### `withAutoPlayDuration`

`withAutoPlayDuration(long duration, TimeUnit timeUnit)` sets the duration of the autoplay animation for the given step. This must match the exact 
duration of the autoplay segment in your AfterEffects project.

#### `withSwipeDuration`

`withSwipeDuration(long duration, TimeUnit timeUnit)` sets the duration of the swipe animation for the given step. This must match the exact 
duration of the swipe segment in your AfterEffects project.

#### `withSwipeForwardInterpolator`

`withSwipeForwardInterpolator(TimeInterpolator timeInterpolator)` sets the interpolator to use to complete the swipe animation if the user stops 
dragging forwards at any point (before starting the autoplay animation, if any). If you don't set this, it will default to the one you set in 
`withDefaultSwipeForwardInterpolator` or a `LinearInterpolator` if you didn't set one.

#### `withSwipeBackwardsInterpolator`

`withSwipeBackwardsInterpolator(TimeInterpolator timeInterpolator)` sets the interpolator to use to complete the swipe animation if the user stops 
dragging backwards at any point. If you don't set this, it will default to the one you set in `withDefaultSwipeBackwardsInterpolator` or a 
`LinearInterpolator` if you didn't set one.

## License

```
   Copyright 2017 Novoda

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
