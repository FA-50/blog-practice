document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("loginForm");
    const button = document.getElementById("loginButton");
    const error = document.getElementById("loginError");
    const success = document.getElementById("loginSuccess");

    if (new URLSearchParams(window.location.search).get("registered") === "true") {
        success.textContent = "회원가입이 완료되었습니다. 로그인해 주세요.";
    }

    form.addEventListener("submit", async event => {
        event.preventDefault();
        button.disabled = true;
        error.textContent = "";

        try {
            const response = await fetch("/api/auth/login", {
                method: "POST",
                credentials: "same-origin",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    userId: document.getElementById("userId").value.trim(),
                    password: document.getElementById("password").value
                })
            });

            if (!response.ok) {
                const body = await response.json().catch(() => ({}));
                throw new Error(body.message || "아이디 또는 비밀번호를 확인해 주세요.");
            }

            window.location.replace(safeRedirectTarget());
        } catch (exception) {
            error.textContent = exception.message;
        } finally {
            button.disabled = false;
        }
    });
});

function safeRedirectTarget() {
    const target = new URLSearchParams(window.location.search).get("redirect");
    if (target && target.startsWith("/") && !target.startsWith("//")) {
        return target;
    }
    return "/posts";
}
