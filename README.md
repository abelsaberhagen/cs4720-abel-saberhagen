Abel Saberhagen
eug8xn

I chose to use SENSOR_DELAY_GAME since it was best for step detection at 50 recordings per second. Faster than that would reduce battery in the phone
for unnecessary data as humans are unlikely to move faster than what 50 recordings per second can possibly detect.

This app could be used for tracking someone's mental and physical health. Someone with depression or some other mental illness will have lower steps than someone with a healthier mental state. It could be used to determine the types of activities they may enjoy partaking in, whether it be sports or indoor activities. To protect user's privacies, I would ideally give an option to delete data exceeding a certain amount of time based on the user's requests. This amount of time could be after 1 week, 1 month, or perhaps indefinitely if the user does not care about their privacy. 

AI was used to help me convert acceleration signals into steps. It first finds the magnitude and then, to smooth, takes the weighted average of the smoothed data and the magnitude. Smoothed will start at 9.81 because that is the acceleration of the phone when resting. There is another check that determines whether the recorded step happened after a certain amount of time. This is done to ignore potential bumps that may go really low and then really high after a step. If the smoothed signal is not higher than the high threshold, then it checks if it is lower than the low threshold and the step already occurred. If this is true, it means that the step is done, which means the series of signals during the time period of the step have ended.
