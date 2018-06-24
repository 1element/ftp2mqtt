package com.github._1element.ftp2mqtt.ftp;

import com.github._1element.ftp2mqtt.common.FilesFacade;
import com.github._1element.ftp2mqtt.common.IncomingFileEvent;
import org.apache.ftpserver.ftplet.DefaultFtpReply;
import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.FtpFile;
import org.apache.ftpserver.ftplet.FtpReply;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.context.ApplicationEventPublisher;

import java.nio.file.Paths;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class FtpActionsTests {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private ApplicationEventPublisher eventPublisher;

  @Mock
  private FilesFacade filesFacade;

  @InjectMocks
  private FtpActions ftpActions;

  @Test
  public void shouldContinueOnUploadStart() throws Exception {
    // given, when
    final FtpletResult result = ftpActions.onUploadStart(null, null);

    // then
    then(result).isEqualTo(FtpletResult.DEFAULT);
  }

  @Test
  public void shouldSkipOnDelete() throws Exception {
    // given
    final FtpSession ftpSession = mock(FtpSession.class);

    // when
    final FtpletResult result = ftpActions.onDeleteStart(ftpSession, null);

    // then
    then(result).isEqualTo(FtpletResult.SKIP);
  }

  @Test
  public void shouldSendNoPermissionReplyOnDelete() throws Exception {
    // given
    final FtpSession ftpSession = mock(FtpSession.class);
    final FtpReply expectedFtpReply = new DefaultFtpReply(FtpReply.REPLY_450_REQUESTED_FILE_ACTION_NOT_TAKEN, "No permission.");

    // when
    ftpActions.onDeleteStart(ftpSession, null);

    // then
    BDDMockito.then(ftpSession).should().write(refEq(expectedFtpReply));
  }

  @Test
  public void shouldSkipOnDownload() throws Exception {
    // given
    final FtpSession ftpSession = mock(FtpSession.class);

    // when
    final FtpletResult result = ftpActions.onDownloadStart(ftpSession, null);

    then(result).isEqualTo(FtpletResult.SKIP);
  }

  @Test
  public void shouldSendNoPermissionReplyOnDownload() throws Exception {
    // given
    final FtpSession ftpSession = mock(FtpSession.class);
    final FtpReply expectedFtpReply = new DefaultFtpReply(FtpReply.REPLY_550_REQUESTED_ACTION_NOT_TAKEN, "No permission.");

    // when
    ftpActions.onDownloadStart(ftpSession, null);

    // then
    BDDMockito.then(ftpSession).should().write(refEq(expectedFtpReply));
  }

  @Test
  public void shouldPublishEventOnUploadEnd() throws Exception {
    // given
    final FtpSession ftpSession = mock(FtpSession.class);
    final FtpRequest ftpRequest = mock(FtpRequest.class);
    final FileSystemView fileSystemView = aFileSystemView();
    given(ftpSession.getFileSystemView()).willReturn(fileSystemView);
    given(ftpSession.getUser()).willReturn(aFtpUser());
    given(filesFacade.readAllBytes(any())).willReturn(aByteArray());
    final IncomingFileEvent expectedIncomingFileEvent = new IncomingFileEvent("username", aByteArray());

    // when
    ftpActions.onUploadEnd(ftpSession, ftpRequest);

    // then
    BDDMockito.then(eventPublisher).should().publishEvent(refEq(expectedIncomingFileEvent));
  }

  @Test
  public void shouldDeleteTemporaryFileOnUploadEnd() throws Exception {
    // given
    final FtpSession ftpSession = mock(FtpSession.class);
    final FtpRequest ftpRequest = mock(FtpRequest.class);
    final FileSystemView fileSystemView = aFileSystemView();
    given(ftpSession.getFileSystemView()).willReturn(fileSystemView);
    given(ftpSession.getUser()).willReturn(aFtpUser());
    given(filesFacade.readAllBytes(any())).willReturn(aByteArray());
    given(ftpRequest.getArgument()).willReturn("file.jpg");

    // when
    ftpActions.onUploadEnd(ftpSession, ftpRequest);

    // then
    BDDMockito.then(filesFacade).should().delete(Paths.get("/home/directory/absolute/path/file.jpg"));
  }

  private FtpUser aFtpUser() {
    return new FtpUser("username", "password", "/home/directory/");
  }

  private FileSystemView aFileSystemView() throws Exception {
    final FtpFile ftpFile = mock(FtpFile.class);
    given(ftpFile.getAbsolutePath()).willReturn("/absolute/path/");

    final FileSystemView fileSystemView = mock(FileSystemView.class);
    given(fileSystemView.getWorkingDirectory()).willReturn(ftpFile);

    return fileSystemView;
  }

  private byte[] aByteArray() {
    return "test data".getBytes();
  }

}
