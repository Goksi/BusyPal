function onBusyStatusChange() {
  const busyToggle = document.getElementById('busyToggle');
  const busy = busyToggle.checked;
  const form = document.getElementById('busyForm');
  const formData = new FormData(form);
  formData.set('busy', busy)

  fetch('/api/busy', {
    method: 'POST',
    body: formData,
    headers: {
      'X-Requested-With': 'XMLHttpRequest'
    }
  }).then(response => {
    if (response.ok) {
      return;
    }
    console.error("Error while changing busy status", response);
    busyToggle.checked = !busy;
    window.alert(
        "Unexpected status code while calling backend: " + response.status);
  }).catch(() => {
    console.error("Error while changing busy status", err);
    busyToggle.checked = !busy;
    window.alert("Unexpected error while calling backend");
  })
}