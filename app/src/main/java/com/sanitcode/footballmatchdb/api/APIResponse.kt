package com.sanitcode.footballmatchdb.api

import com.sanitcode.footballmatchdb.model.MatchModel

data class APIResponse(
        val events: List<MatchModel>,
        val teams: List<MatchModel>
)