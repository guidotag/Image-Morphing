
public class IMCoupleCoordinator {
	private IMImagePanel srcImagePanel;
	private IMImagePanel dstImagePanel;

	public IMCoupleCoordinator(IMImagePanel srcImagePanel, IMImagePanel dstImagePanel) {
		this.srcImagePanel = srcImagePanel;
		this.dstImagePanel = dstImagePanel;

		this.srcImagePanel.setCoordinator(this);
		this.dstImagePanel.setCoordinator(this);

		this.srcImagePanel.enableMouseListener();
	}

	public void notifySegmentFinished(IMImagePanel activePanel) {
		IMImagePanel otherPanel;

		if (activePanel == this.srcImagePanel) {
			otherPanel = this.dstImagePanel;
		} else {
			otherPanel = this.srcImagePanel;
		}

		activePanel.disableMouseListener();
		otherPanel.enableMouseListener();
	}
}