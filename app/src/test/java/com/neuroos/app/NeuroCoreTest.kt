package com.neuroos.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NeuroCoreTest {
    @Test
    fun defaultProfileKeepsCalmAiEnabled() {
        val profile = NeuroProfile()

        assertEquals("college", profile.studentSegment)
        assertTrue(profile.calmAiEnabled)
        assertEquals(VisualProfiles.calm, profile.sensoryProfile)
    }

    @Test
    fun sessionStartsAtFirstSmallestStep() {
        val task = NeuroRepository.tasks.first()
        val session = createFocusSession(task)

        assertEquals(SessionStatus.Ready, session.status)
        assertEquals("Open Chapter 8 notes", currentStep(task, session))
        assertEquals(0f, sessionProgress(task, session))
    }

    @Test
    fun timerWarnsAtFiveMinuteTransition() {
        val task = NeuroRepository.tasks.first()
        val started = reduceSession(createFocusSession(task, durationMinutes = 6), SessionAction.Start(0))
        val warned = reduceSession(started, SessionAction.Tick(seconds = 61, nowMillis = 61_000))

        assertEquals(SessionStatus.Active, warned.status)
        assertEquals("Five minute transition", warned.transitionWarning)
    }

    @Test
    fun completingStepAdvancesCurrentTarget() {
        val task = NeuroRepository.tasks.first()
        val next = reduceSession(createFocusSession(task), SessionAction.CompleteStep)

        assertEquals(setOf(0), next.completedStepIndexes)
        assertEquals("Mark three concepts that feel unclear", currentStep(task, next))
    }

    @Test
    fun timeFormattingIsStable() {
        assertEquals("00:00", formatRemainingTime(0))
        assertEquals("01:05", formatRemainingTime(65))
        assertEquals("25:00", formatRemainingTime(25 * 60))
    }

    @Test
    fun completedSessionCannotResume() {
        val task = NeuroRepository.tasks.first()
        val completed = reduceSession(
            createFocusSession(task),
            SessionAction.CompleteSession(nowMillis = 1_000)
        )

        assertEquals(SessionStatus.Complete, reduceSession(completed, SessionAction.Resume).status)
        assertEquals(SessionStatus.Complete, reduceSession(completed, SessionAction.Start()).status)
    }

    @Test
    fun completingStepsStopsAtTaskBoundary() {
        val task = NeuroRepository.tasks.first()
        var session = createFocusSession(task)
        repeat(task.steps.size + 2) {
            session = reduceSession(session, SessionAction.CompleteStep)
        }

        assertEquals(task.steps.size, session.activeStepIndex)
        assertEquals(task.steps.indices.toSet(), session.completedStepIndexes)
        assertEquals(1f, sessionProgress(task, session))
    }

    @Test
    fun negativeTickCannotAddTime() {
        val task = NeuroRepository.tasks.first()
        val started = reduceSession(createFocusSession(task), SessionAction.Start(nowMillis = 0))

        assertEquals(started.remainingSeconds, reduceSession(started, SessionAction.Tick(seconds = -10)).remainingSeconds)
    }

    @Test
    fun firstEditionThemeRecommendationUsesOptionalAgeGuidance() {
        assertEquals(FirstEditionThemes.kids, FirstEditionThemes.recommend("6-9"))
        assertEquals(FirstEditionThemes.momentum, FirstEditionThemes.recommend("14-17"))
        assertEquals(FirstEditionThemes.evergreen, FirstEditionThemes.recommend("25-plus"))
    }

    @Test
    fun unknownThemeAndSkippedAgeGuidanceFallBackToCyber() {
        assertEquals(FirstEditionThemes.cyber, FirstEditionThemes.find("unknown"))
        assertEquals(FirstEditionThemes.cyber, FirstEditionThemes.recommend("not-set"))
    }
}
