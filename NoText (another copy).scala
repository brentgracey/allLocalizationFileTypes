package com.qordoba.reporting.v2.velocity.controller.behaviour

import java.util.logging.Logger

import com.qordoba.reporting.v2.utils.GetQReportingV2Config
import com.qordoba.reporting.v2.velocity.model.api._
import com.qordoba.reporting.v2.velocity.model.dao.{ProjectActivityDao, TranslationEventDao}
import com.qordoba.reporting.v2.velocity.model.store.bigtable.GenerateCycleBigTable
import com.qordoba.reporting.v2.velocity.model.api.queryby.{DayPage, DayUser, TranslationEventDayQuery, TranslationEventReportQuery}
import com.qordoba.reporting.v2.velocity.model.apiutils.{DayPageUtils, DayUserUtils, ProjectActivitySegmentQueryUtils, TranslationEventDayQueryUtils}
import com.qordoba.reporting.v2.velocity.model.transaction._
import play.api.Configuration
import play.api.db.Database
import play.api.libs.json.Json

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.Iterable
import scala.concurrent.{ExecutionContext, Future}


// MariaDB Based Check... might be needed if data is not migrated
/*val areDuplicatesOfTheseSegmentsAlreadyPushed : Seq[SegmentTextAlreadyPushedInPage] = AnormSql.areDuplicatesOfTheseSegmentsAlreadyPushed(
  recordTranslationEvents.projectId,
  recordTranslationEvents.translationEvents.map{_.segmentId},
  recordTranslationEvents.futureMilestoneIds,
  cycleId
)(configuration, db)*/

// MariaDB Based Check... might be needed if data is not migrated
//val alreadyPushedInPages = areDuplicatesOfTheseSegmentsAlreadyPushed.filter{_.original_plain == doIHaveDuplicates.sourceSegment}.map{_.page_id}

class VelocityOfTranslationsOverTimeBehavior(tmMatchPercentageRanges: Seq[VelocityReportTmMatchPercentageRange], cycleId: String)(
  translationEventDao : TranslationEventDao,
  projectActivityDao : ProjectActivityDao,
  generateCycleBigTable: GenerateCycleBigTable,
  qReportingV2Config: GetQReportingV2Config
)(
  implicit ec: ExecutionContext
) {

  val logger = Logger.getLogger(this.getClass.getName)
