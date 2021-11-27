package fr.zenigata.game;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import fr.zenigata.data.Fiction;
import fr.zenigata.util.SpecUtils;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class TriviaGame {

  private static final Logger logger = LoggerFactory.getLogger(TriviaGame.class);

  private List<Fiction> fictions;
  private final List<String> answers;
  private Instant startTimer;

  protected final Duration duration;
  protected final AtomicBoolean isScheduled;
  protected Disposable scheduledTask;

  private MessageCreateEvent event;

  public TriviaGame(MessageCreateEvent event, List<Fiction> fictions) {
    this.event = event;
    this.fictions = fictions;
    this.answers = new ArrayList<>();
    this.duration = Duration.ofSeconds(30);
    this.isScheduled = new AtomicBoolean(false);
  }

  public void start() {
    this.answers.add("Adoprixtoxis");
    this.answers.add("Blind Cowboy");
    this.answers.add("HERO");
    this.answers.add("1287");
    Collections.shuffle(this.answers);

    this.schedule(this.end());
    this.startTimer = Instant.now();
    TriviaInputs.create(this.event.getClient(), this).listen();
  }

  public Mono<Message> show() {
    return Mono.defer(() -> {
      final String description = "Dans quelle fiction sonore ?";

      return event.getMessage().getChannel().flatMap(c -> c.createEmbed(SpecUtils.displayQuote(fictions.get(0))));
    });
  }

  public Mono<Void> end() {
    return Mono
        .fromRunnable(
            () -> this.event.getMessage().getChannel().flatMap(c -> c.createMessage("La réponse était : 1287")))
        .then(Mono.fromRunnable(this::destroy));
  }

  public Object destroy() {
    return null;
  }

  protected <T> void schedule(Mono<T> mono) {
    this.cancelScheduledTask();
    this.isScheduled.set(true);
    this.scheduledTask = Mono.delay(this.duration, Schedulers.boundedElastic())
        .doOnNext(__ -> this.isScheduled.set(false)).then(mono).subscribe(null, err -> handleUnknownError(err));
  }

  private void cancelScheduledTask() {
    if (this.isScheduled()) {
      this.scheduledTask.dispose();
      this.isScheduled.set(false);
    }
  }

  public boolean isScheduled() {
    return this.scheduledTask != null && this.isScheduled.get();
  }

  private void handleUnknownError(Throwable err) {
    logger.error(String.format("An unknown error occurred: %s", Objects.requireNonNullElse(err.getMessage(), "")), err);
  }

}
