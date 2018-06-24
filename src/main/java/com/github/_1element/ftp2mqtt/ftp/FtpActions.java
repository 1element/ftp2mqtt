package com.github._1element.ftp2mqtt.ftp;

import com.github._1element.ftp2mqtt.common.FilesFacade;
import com.github._1element.ftp2mqtt.common.IncomingFileEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.ftplet.DefaultFtpReply;
import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpReply;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@Component
public class FtpActions extends DefaultFtplet {

  private final ApplicationEventPublisher eventPublisher;
  private final FilesFacade filesFacade;

  private static final String SEPARATOR = "/";
  private static final String NO_PERMISSION_MESSAGE = "No permission.";

  @Override
  public FtpletResult onUploadStart(final FtpSession session, final FtpRequest request) throws FtpException, IOException {
    return FtpletResult.DEFAULT;
  }

  @Override
  public FtpletResult onUploadEnd(final FtpSession session, final FtpRequest request) throws FtpException, IOException {
    log.debug("File '{}' was uploaded to ftp server.", request.getArgument());

    final String userName = session.getUser().getName();
    final Path filePath = getFilePath(session, request);
    final byte[] fileData = readAndDeleteTemporaryFile(filePath);

    eventPublisher.publishEvent(new IncomingFileEvent(userName, fileData));

    return super.onUploadEnd(session, request);
  }

  @Override
  public FtpletResult onDeleteStart(final FtpSession session, final FtpRequest request) throws FtpException, IOException {
    session.write(new DefaultFtpReply(FtpReply.REPLY_450_REQUESTED_FILE_ACTION_NOT_TAKEN, NO_PERMISSION_MESSAGE));
    return FtpletResult.SKIP;
  }

  @Override
  public FtpletResult onDownloadStart(final FtpSession session, final FtpRequest request) throws FtpException, IOException {
    session.write(new DefaultFtpReply(FtpReply.REPLY_550_REQUESTED_ACTION_NOT_TAKEN, NO_PERMISSION_MESSAGE));
    return FtpletResult.SKIP;
  }

  private Path getFilePath(final FtpSession session, final FtpRequest request) throws FtpException {
    final String homeDirectory = session.getUser().getHomeDirectory();
    final String workingDirectory = session.getFileSystemView().getWorkingDirectory().getAbsolutePath();
    final String fileArgument = request.getArgument();

    return Paths.get(homeDirectory + workingDirectory + SEPARATOR + fileArgument);
  }

  private byte[] readAndDeleteTemporaryFile(final Path filePath) throws IOException {
    final byte[] fileData = filesFacade.readAllBytes(filePath);
    filesFacade.delete(filePath);

    return fileData;
  }

}
