class CustomElement extends HTMLElement {
    
    static _getTemplate() {
        const result = document.createElement('template');
        result.innerHTML = `
            <link rel="stylesheet" href="./custom-element.css">
            <div id="custom-elemen-root">
                <slot></slot>
            </div>
        `;
        return result;
    }
   
    constructor() {
        super();
        this.attachShadow({mode: 'open'});
        this.shadowRoot.appendChild(CustomElement._getTemplate().content.cloneNode(true));
    }
    
	set text(text) {
		this.innerHTML = text;
    }
}

customElements.define('custom-element', CustomElement);